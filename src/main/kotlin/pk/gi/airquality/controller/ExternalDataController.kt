package pk.gi.airquality.controller

import org.apache.logging.log4j.LogManager
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import pk.gi.airquality.Exception.IllegalFormulaException
import pk.gi.airquality.model.rest.out.*
import pk.gi.airquality.service.DataProviderService

@RestController
@CrossOrigin(origins = ["http://localhost:5173"])
class ExternalDataController(
    val dataProviderService: DataProviderService
) {
    @GetMapping("/time/{interval}/parameter/{parameterFormula}")
    suspend fun getLastIntervalData(@PathVariable parameterFormula: String, @PathVariable interval: Number): ResponseEntity<List<CityStations>> {
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
    suspend fun getLastIntervalDataVoivodeships(@PathVariable parameterFormula: String, @PathVariable interval: Number): ResponseEntity<List<VoivodeshipCity>> {
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
    suspend fun getAirQualityIndex(@PathVariable interval: Number): List<AirQualityIndex> {
        return dataProviderService.getAllIndexAfterDate(interval)
    }

    @GetMapping("/stations/all")
    suspend fun getAirQualityIndex():  List<StationDTO>{
        return dataProviderService.getAllStations()
    }

    @GetMapping("/time/{interval}/average")
    suspend fun getAverageValues(@PathVariable interval: Number):  List<AverageValue> {
        return dataProviderService.getAverageValues(interval)
    }

    @GetMapping("/stations/{stationId}/time/{interval}")
    suspend fun getValuesForStation(@PathVariable stationId: Long, @PathVariable interval: Number): List<GraphValue> {
        return dataProviderService.getGraphValues(stationId, interval)
    }
    @GetMapping("/stations/all/time/{interval}/average")
    suspend fun getAverageValuesForStation(@PathVariable interval: Number): List<GraphAverageVoivodeshipValue> {
        return dataProviderService.getGraphAverageVoivodeshipValues(interval)
    }
    companion object {
        private val logger = LogManager.getLogger()
    }

}
