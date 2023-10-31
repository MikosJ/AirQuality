package pk.gi.airquality.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import pk.gi.airquality.model.rest.out.StationData
import pk.gi.airquality.service.DataProviderService

@RestController
class ExternalDataController(
    val dataProviderService: DataProviderService
) {
    @GetMapping("/ext/days")
    suspend fun getLast3DaysData(): List<StationData> {
        return dataProviderService.getLast3DaysData()
    }

    @GetMapping("/ext/last")
    suspend fun getLastHourData(): List<StationData> {
        return dataProviderService.getLastAddedSensorData()
    }
}
