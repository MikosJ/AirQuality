package pk.gi.airquality.service

import org.springframework.http.HttpMethod
import org.springframework.http.RequestEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.net.URI

@Service
class GiosDataImportService(
        val restTemplate: RestTemplate
) {
    fun getAllStations(): String {
        return restTemplate.exchange(URI("https://api.gios.gov.pl/pjp-api/rest/station/findAll"), HttpMethod.GET, RequestEntity.EMPTY, String::class.java).body.toString()
    }
}