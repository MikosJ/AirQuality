package pk.gi.airquality.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import pk.gi.airquality.model.rest.SensorData
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

    @GetMapping("/stations/save")
    fun saveAllStations() {
        return giosDataImportService.saveAllStations()
    }

    @GetMapping("/stations/sensors/save")
    fun saveSensors() {
        return giosDataImportService.saveSensors()
    }

    @GetMapping("/stations/{stationId}")
    fun getSensorsForStation(@PathVariable stationId: Long): List<pk.gi.airquality.model.rest.Sensor> {
        return giosDataImportService.getSensorsForSingleStation(stationId)
    }

    @GetMapping("/stations/{stationId}/{sensorId}")
    fun getDataForSensor(@PathVariable sensorId: Long): SensorData {
        return giosDataImportService.getDataForSensor(sensorId)
    }
}
