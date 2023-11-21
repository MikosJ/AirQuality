package pk.gi.airquality.controller

import org.apache.logging.log4j.LogManager
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import pk.gi.airquality.Exception.IllegalFormulaException
import pk.gi.airquality.db.model.AirQualityIndex
import pk.gi.airquality.db.model.Station
import pk.gi.airquality.model.rest.out.CityStations
import pk.gi.airquality.model.rest.out.StationDTO
import pk.gi.airquality.model.rest.out.VoivodeshipCity
import pk.gi.airquality.service.DataProviderService

@RestController
@CrossOrigin(origins = ["http://localhost:5173"])
class ExternalDataController(
    val dataProviderService: DataProviderService
) {
    @GetMapping("/time/{interval}/parameter/{parameterFormula}")
    suspend fun getLastHourData(@PathVariable parameterFormula: String, @PathVariable interval: Number): ResponseEntity<List<CityStations>> {
        runCatching {
            return ResponseEntity(dataProviderService.getDataForParameterFormula(parameterFormula, interval), HttpStatus.OK)
        }
            .onFailure { e ->
                if (e is IllegalFormulaException) {
                    logger.error("Parameter formula $parameterFormula is not supported")
                    return ResponseEntity(emptyList(), HttpStatus.NOT_FOUND)
                }
            }
        return ResponseEntity(emptyList(), HttpStatus.NOT_FOUND)
    }

    @GetMapping("/time/{interval}/parameter/{parameterFormula}/voivodeships")
    suspend fun getLastHourDataVoivodeships(@PathVariable parameterFormula: String, @PathVariable interval: Number): ResponseEntity<List<VoivodeshipCity>> {
        runCatching {
            return ResponseEntity(dataProviderService.getDataForParameterFormulaByVoivodeship(parameterFormula, interval), HttpStatus.OK)
        }
            .onFailure { e ->
                if (e is IllegalFormulaException) {
                    logger.error("Parameter formula $parameterFormula is not supported")
                    return ResponseEntity(emptyList(), HttpStatus.NOT_FOUND)
                }
            }
        return ResponseEntity(emptyList(), HttpStatus.NOT_FOUND)
    }

    @GetMapping("/stations/index/{interval}")
    suspend fun getAirQualityIndex(@PathVariable interval: Number): List<pk.gi.airquality.model.rest.out.AirQualityIndex> {

        return dataProviderService.getAllIndexAfterDate(interval)
    }

    @GetMapping("/stations/all")
    suspend fun getAirQualityIndex():  List<StationDTO>{
        return dataProviderService.getAllStations()
    }
    companion object {
        private val logger = LogManager.getLogger()
    }

}
