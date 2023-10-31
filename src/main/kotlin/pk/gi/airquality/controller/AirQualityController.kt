package pk.gi.airquality.controller

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PathVariable
import pk.gi.airquality.model.rest.SensorData
import pk.gi.airquality.model.rest.Stations
import pk.gi.airquality.service.GIOSDataImportService

@RestController
@CrossOrigin(origins = ["http://localhost:3000"])
class AirQualityController(
    val giosDataImportService: GIOSDataImportService
) {

    @PostMapping("/stations")
    suspend fun saveAllStations() {
        withContext(Dispatchers.IO) {
            giosDataImportService.saveAllStations()
        }
    }

    @PostMapping("/stations/sensors")
    suspend fun saveSensors() {
        withContext(Dispatchers.IO) {
            giosDataImportService.saveSensors()
        }
    }

    @PostMapping("/stations/sensors/data")
    suspend fun saveSensorData() {
        withContext(Dispatchers.IO) {
            giosDataImportService.saveSensorData()
        }
    }

    @GetMapping("/stations/{stationId}")
    fun getSensorsForStation(@PathVariable stationId: Long): List<pk.gi.airquality.model.rest.Sensor> {
        return giosDataImportService.getSensorsForSingleStation(stationId)
    }

    @GetMapping("/stations")
    fun getAllStations(): Stations {
        return giosDataImportService.getAllStations()
    }

    @GetMapping("/stations/{stationId}/sensors/{sensorId}")
    fun getDataForSensor(@PathVariable sensorId: Long): SensorData {
        return giosDataImportService.getDataForSensor(sensorId)
    }

}
