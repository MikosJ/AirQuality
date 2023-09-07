package pk.gi.airquality.service

import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.RequestEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import pk.gi.airquality.model.rest.Sensor
import pk.gi.airquality.model.rest.SensorData
import pk.gi.airquality.model.rest.Sensors
import pk.gi.airquality.model.rest.Stations
import pk.gi.airquality.model.rest.Station
import java.net.URI

@Service
class GiosDataImportService(
        val restTemplate: RestTemplate
) {
    fun getAllStations(): Stations {
        return Stations(restTemplate.exchange(
                URI("https://api.gios.gov.pl/pjp-api/rest/station/findAll"),
                HttpMethod.GET,
                RequestEntity.EMPTY,
                typeReference<List<Station>>()
        ).body!!)
    }


    fun getSensorsForSingleStation(stationId: Long): Sensors {
        return Sensors(restTemplate.exchange(
                URI("https://api.gios.gov.pl/pjp-api/rest/station/sensors/$stationId"),
                HttpMethod.GET,
                RequestEntity.EMPTY,
                typeReference<List<Sensor>>()
        ).body!!)
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
