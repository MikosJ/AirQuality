package pk.gi.airquality.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import pk.gi.airquality.model.rest.Stations
import pk.gi.airquality.service.GiosDataImportService

@RestController()
class AirQualityController(
        val giosDataImportService: GiosDataImportService
) {
    @GetMapping("/stations")
    fun getAllStations(): Stations {
        return giosDataImportService.getAllStations()
    }
}
