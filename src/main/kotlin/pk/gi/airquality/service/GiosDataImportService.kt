package pk.gi.airquality.service

import jakarta.transaction.Transactional
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.RequestEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import pk.gi.airquality.db.model.City
import pk.gi.airquality.db.model.Parameter
import pk.gi.airquality.db.service.*
import pk.gi.airquality.model.rest.*
import java.net.URI
import kotlin.jvm.optionals.getOrNull

@Service
class GiosDataImportService(
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

    fun saveAllStations() {
        val body =
            restTemplate.exchange(
                URI("https://api.gios.gov.pl/pjp-api/rest/station/findAll"),
                HttpMethod.GET,
                RequestEntity.EMPTY,
                typeReference<List<Station>>()
            ).body!!
        cityRepository.saveAll(body.map {
            City(it.city.id, it.city.name, it.city.commune.name, it.city.commune.district, it.city.commune.province)
        })

        stationRepository.saveAll(
            body.map {
                pk.gi.airquality.db.model.Station(
                    it.id,
                    it.name,
                    it.latitude,
                    it.longitude,
                    cityRepository.findById(it.city.id).getOrNull(),
                    it.addressStreet
                )
            }
        )
    }

    fun saveSensors() {
        stationRepository.findAll().forEach { station ->
            val body = restTemplate.exchange(
                URI("https://api.gios.gov.pl/pjp-api/rest/station/sensors/${station.stationId}"),
                HttpMethod.GET,
                RequestEntity.EMPTY,
                typeReference<List<Sensor>>()
            ).body!!
            val params = body.map { sensor ->
                Parameter(sensor.param.name, sensor.param.formula, sensor.param.code, sensor.param.id)
            }
            parameterRepository.saveAll(params)
            val sensors = body.map { sensor ->
                pk.gi.airquality.db.model.Sensor(sensor.id, params)
            }
            sensorRepository.saveAll(sensors)
        }
    }

    fun saveSensorData() {
        sensorRepository.findAll().forEach { sensor ->
            val body = restTemplate.exchange(
                URI("https://api.gios.gov.pl/pjp-api/rest/data/getData/${sensor.id}"),
                HttpMethod.GET,
                HttpEntity.EMPTY,
                SensorData::class.java
            ).body!!
            if (body.values.isNotEmpty() && body.values[0].value != null) {
                if (!(sensorDataRepository.existsByDateAndSensorId(body.values[0].date!!, sensor.id))) {
                    body.values[0].let {
                        val param = parameterRepository.findFirstByParamCode(body.key)
                        sensorDataRepository.save(
                            pk.gi.airquality.db.model.SensorData(
                                sensor = sensor,
                                date = it.date,
                                value = it.value,
                                parameter = param
                            )
                        )
                    }
                }
            }


//            body.values.forEach { value ->
//                val param = parameterRepository.findFirstByParamCode(body.key)
//                sensorDataRepository.save(
//                    pk.gi.airquality.db.model.SensorData(
//                        sensor = sensor,
//                        date = value.date,
//                        value = value.value,
//                        parameter = param
//                    )
//                )
//            }
        }
    }


    fun getSensorsForSingleStation(stationId: Long): List<Sensor> {
        val body =
            restTemplate.exchange(
                URI("https://api.gios.gov.pl/pjp-api/rest/station/sensors/$stationId"),
                HttpMethod.GET,
                RequestEntity.EMPTY,
                typeReference<List<Sensor>>()
            ).body!!
        return body
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
