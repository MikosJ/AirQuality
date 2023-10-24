package pk.gi.airquality.service

import jakarta.transaction.Transactional
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.RequestEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import pk.gi.airquality.db.model.City
import pk.gi.airquality.db.model.Parameter
import pk.gi.airquality.db.service.CityRepository
import pk.gi.airquality.db.service.SensorDataRepository
import pk.gi.airquality.db.service.SensorRepository
import pk.gi.airquality.db.service.StationRepository
import pk.gi.airquality.db.service.ParameterRepository
import pk.gi.airquality.model.rest.Station
import pk.gi.airquality.model.rest.SensorData
import pk.gi.airquality.model.rest.Sensor
import pk.gi.airquality.model.rest.Stations
import java.math.BigDecimal
import java.net.URI
import kotlin.jvm.optionals.getOrNull

@Service
class GIOSDataImportService(
    val restTemplate: RestTemplate,
    val sensorRepository: SensorRepository,
    val stationRepository: StationRepository,
    val parameterRepository: ParameterRepository,
    val cityRepository: CityRepository,
    val sensorDataRepository: SensorDataRepository
) {
    fun getAllStations(): Stations {
        return Stations(
            restTemplate.exchange(
                URI("https://api.gios.gov.pl/pjp-api/rest/station/findAll"),
                HttpMethod.GET,
                RequestEntity.EMPTY,
                typeReference<List<Station>>()
            ).body!!
        )
    }

    suspend fun saveAllStations(): MutableIterable<pk.gi.airquality.db.model.Station> = coroutineScope {

        val response = restTemplate.exchange(
            URI("https://api.gios.gov.pl/pjp-api/rest/station/findAll"),
            HttpMethod.GET,
            RequestEntity.EMPTY,
            typeReference<List<Station>>()
        ).body!!

        val cityEntities = response.map {
            async(Dispatchers.IO) {
                City(it.city.id, it.city.name, it.city.commune.name, it.city.commune.district, it.city.commune.province)
            }
        }
        cityRepository.saveAll(cityEntities.awaitAll())

        val stationEntities = response.map {
            async(Dispatchers.IO) {
                val city = cityRepository.findById(it.city.id).getOrNull()
                pk.gi.airquality.db.model.Station(
                    it.id,
                    it.name,
                    it.latitude,
                    it.longitude,
                    city,
                    it.addressStreet
                )
            }
        }

        stationRepository.saveAll(stationEntities.awaitAll())
    }

    suspend fun saveSensors() = coroutineScope {
        val stations = stationRepository.findAll()

        val sensorAndParameterJobs = stations.map { station ->
            async {
                val response = restTemplate.exchange(
                    URI("https://api.gios.gov.pl/pjp-api/rest/station/sensors/${station.stationId}"),
                    HttpMethod.GET,
                    RequestEntity.EMPTY,
                    typeReference<List<Sensor>>()
                ).body ?: emptyList()
                val sensorsToSave = mutableListOf<pk.gi.airquality.db.model.Sensor>()
                val parametersToSave = mutableListOf<Parameter>()

                for (sensor in response) {
                    if (!parameterRepository.existsByIdParam(sensor.param.id)) {
                        parametersToSave.add(
                            Parameter(
                                paramName = sensor.param.name,
                                paramFormula = sensor.param.formula,
                                paramCode = sensor.param.code,
                                idParam = sensor.param.id
                            )
                        )
                    }
                    sensorsToSave.add(pk.gi.airquality.db.model.Sensor(sensor.id, station))
                }

                sensorRepository.saveAll(sensorsToSave)
                parameterRepository.saveAll(parametersToSave)
            }
        }

        sensorAndParameterJobs.awaitAll()
    }

    suspend fun saveSensorData() = coroutineScope {
        val sensors = sensorRepository.findAll()

        val deferredResults = sensors.map { sensor ->
            async(Dispatchers.IO) {
                val response = restTemplate.exchange(
                    URI("https://api.gios.gov.pl/pjp-api/rest/data/getData/${sensor.id}"),
                    HttpMethod.GET,
                    HttpEntity.EMPTY,
                    SensorData::class.java
                ).body

                if (response?.values?.isNotEmpty() == true && response.values[0].value != null) {
                    if (!sensorDataRepository.existsByDateAndSensorId(response.values[0].date!!, sensor.id)) {
                        val param = parameterRepository.findFirstByParamCode(response.key)

                        val sensorData = pk.gi.airquality.db.model.SensorData(
                            sensor = sensor,
                            date = response.values[0].date,
                            value = BigDecimal.valueOf(response.values[0].value ?: -1.0),
                            parameter = param
                        )
                        if (sensorData.value != BigDecimal.valueOf(-1.0)) {
                            sensorDataRepository.save(sensorData)
                        }
                    }
                }
            }
        }

        deferredResults.awaitAll()
    }


    fun getSensorsForSingleStation(stationId: Long): List<Sensor> {
        return restTemplate.exchange(
            URI("https://api.gios.gov.pl/pjp-api/rest/station/sensors/$stationId"),
            HttpMethod.GET,
            RequestEntity.EMPTY,
            typeReference<List<Sensor>>()
        ).body!!
    }

    fun getDataForSensor(sensorId: Long): SensorData {
        return restTemplate.exchange(
            URI("https://api.gios.gov.pl/pjp-api/rest/data/getData/$sensorId"),
            HttpMethod.GET,
            HttpEntity.EMPTY,
            SensorData::class.java
        ).body!!
    }

    @Transactional
    fun deleteAllNullValues() {
        sensorDataRepository.deleteAllByValueNull()
    }

}

inline fun <reified T> typeReference() = object : ParameterizedTypeReference<T>() {}
