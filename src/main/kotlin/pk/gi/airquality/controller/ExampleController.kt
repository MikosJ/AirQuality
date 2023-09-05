package pk.gi.airquality.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import pk.gi.airquality.service.GiosDataImportService

@RestController
class ExampleController(
        val giosDataImportService: GiosDataImportService
) {
    @GetMapping("/test")
    fun getAllStations(): String {
        return giosDataImportService.getAllStations()
    }
}