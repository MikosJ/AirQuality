package pk.gi.airquality.service

import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.RequestEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import pk.gi.airquality.db.service.SensorRepository
import pk.gi.airquality.db.service.StationRepository
import pk.gi.airquality.model.rest.Sensor
import pk.gi.airquality.model.rest.SensorData
import pk.gi.airquality.model.rest.Stations
import pk.gi.airquality.model.rest.Station
import java.net.URI

@Service
class GiosDataImportService(
    val restTemplate: RestTemplate,
    val sensorRepository: SensorRepository,
    val stationRepository: StationRepository
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

        body.forEach { station ->
            stationRepository.save(
                pk.gi.airquality.db.model.Station(
                    station.id,
                    station.name,
                    station.latitude,
                    station.longitude
                )
            )
        }
    }

    fun saveSensorData() {
        getAllStations().stations.forEach { station ->
            restTemplate.exchange(
                URI("https://api.gios.gov.pl/pjp-api/rest/station/sensors/${station.id}"),
                HttpMethod.GET,
                RequestEntity.EMPTY,
                typeReference<List<pk.gi.airquality.db.model.Sensor>>()
            ).body!!.forEach { sensor ->
                sensorRepository.save(
                    pk.gi.airquality.db.model.Sensor(
                        sensor.id,
                        pk.gi.airquality.db.model.Station(
                            station.id,
                            station.name,
                            station.latitude,
                            station.longitude
                        ),
                        sensor.parameter
                    )
                )
            }
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

}

inline fun <reified T> typeReference() = object : ParameterizedTypeReference<T>() {}
