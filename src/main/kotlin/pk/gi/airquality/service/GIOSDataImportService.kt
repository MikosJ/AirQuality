package pk.gi.airquality.service

import org.apache.logging.log4j.LogManager
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.RequestEntity
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import pk.gi.airquality.db.model.City
import pk.gi.airquality.db.model.Parameter
import pk.gi.airquality.db.service.*
import pk.gi.airquality.model.rest.*
import pk.gi.airquality.model.rest.out.AirQualityIndex
import java.math.BigDecimal
import java.net.URI
import kotlin.jvm.optionals.getOrNull

@Service
@EnableAsync
class GIOSDataImportService(
    val restTemplate: RestTemplate,
    val sensorRepository: SensorRepository,
    val stationRepository: StationRepository,
    val parameterRepository: ParameterRepository,
    val cityRepository: CityRepository,
    val sensorDataRepository: SensorDataRepository,
    val airQualityIndexRepository: AirQualityIndexRepository
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

    @Scheduled(cron = "0 30 * * * *")
    fun saveAllStations() {

        val response = restTemplate.exchange(
            URI("https://api.gios.gov.pl/pjp-api/rest/station/findAll"),
            HttpMethod.GET,
            RequestEntity.EMPTY,
            typeReference<List<Station>>()
        ).body!!

        val cityEntities = response.map {
            City(it.city.id, it.city.name, it.city.commune.name, it.city.commune.district, it.city.commune.province)
        }
        cityRepository.saveAll(cityEntities)
        val stationEntities = response.map {

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

        stationRepository.saveAll(stationEntities)
    }

    @Scheduled(cron = "0 35 * * * *")
    fun saveSensors() {
        val stations = stationRepository.findAll()
        stations.map { station ->
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

                    sensorsToSave.add(pk.gi.airquality.db.model.Sensor(sensor.id, station))
                }

                sensorRepository.saveAll(sensorsToSave)
                parameterRepository.saveAll(parametersToSave)
            }
        }
    }

    @Scheduled(cron = "0 45 * * * *")
    fun saveSensorData() {
        val sensors = sensorRepository.findAll()
        sensors.map { sensor ->
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
                        parameterName = param.paramName,
                        parameterFormula = param.paramFormula
                    )
                    if (sensorData.value != BigDecimal.valueOf(-1.0)) {
                        sensorDataRepository.save(sensorData)
                    }
                }
            }
        }
    }

    @Scheduled(cron = "0 01 * * * *")
    fun saveAirQualityIndex() {
        val stations = stationRepository.findAll()
        val indexList = mutableListOf<pk.gi.airquality.db.model.AirQualityIndex>()
        stations.map { station ->
            val aqIndex = restTemplate.exchange(
                URI("https://api.gios.gov.pl/pjp-api/rest/aqindex/getIndex/${station.stationId}"),
                HttpMethod.GET,
                RequestEntity.EMPTY,
                pk.gi.airquality.model.rest.AirQualityIndex::class.java
            ).body
            if (aqIndex != null) {
                if (aqIndex.indexLevel != null && aqIndex.stCalcDate != null && aqIndex.indexLevel.id != null && aqIndex.indexLevel.indexLevelName != null) {
                    println(aqIndex.indexLevel.id)
                    val aqi = pk.gi.airquality.db.model.AirQualityIndex(
                        stationId = station,
                        stCalcDate = aqIndex.stCalcDate,
                        indexLevelId = aqIndex.indexLevel.id.toLong(),
                        indexLevelName = aqIndex.indexLevel.indexLevelName
                    )
                    if (airQualityIndexRepository.findAllByStCalcDateAndStationId(aqIndex.stCalcDate, station)
                            .contains(aqi)
                    ) {
                        logger.info("Aqi already exists, passing...")
                    } else {
                        indexList.add(aqi)
                    }
                }
            }
        }
        airQualityIndexRepository.saveAll(indexList)

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

    companion object {
        const val HOUR = 3600000L
        const val TWENTY_SECONDS = 20000L
        const val MINUTE = 60000L

        private val logger = LogManager.getLogger()

    }

}

inline fun <reified T> typeReference() = object : ParameterizedTypeReference<T>() {}
