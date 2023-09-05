package pk.gi.airquality.service

import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpMethod
import org.springframework.http.RequestEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import pk.gi.airquality.model.rest.Station
import pk.gi.airquality.model.rest.Stations
import java.net.URI

@Service
class GiosDataImportService(
    val restTemplate: RestTemplate
) {
    fun getAllStations(): Stations {
        val output = restTemplate.exchange(
            URI("https://api.gios.gov.pl/pjp-api/rest/station/findAll"),
            HttpMethod.GET,
            RequestEntity.EMPTY,
            typeReference<List<Station>>()
        ).body!!
        return Stations(output)
    }


    fun getMeasurementStationForSingleStation(stationId: Long): String {
        return restTemplate.exchange(
            URI("https://api.gios.gov.pl/pjp-api/rest/station/sensors/$stationId"),
            HttpMethod.GET,
            RequestEntity.EMPTY,
            String::class.java
        ).body.toString()
    }


}

inline fun <reified T> typeReference() = object : ParameterizedTypeReference<T>() {}
