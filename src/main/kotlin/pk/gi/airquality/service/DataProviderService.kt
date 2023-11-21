package pk.gi.airquality.service

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.stereotype.Service
import pk.gi.airquality.Exception.IllegalFormulaException
import pk.gi.airquality.db.service.AirQualityIndexRepository
import pk.gi.airquality.db.service.SensorDataRepository
import pk.gi.airquality.db.service.StationRepository
import pk.gi.airquality.mapper.ResultMapper
import pk.gi.airquality.model.rest.out.AverageValues
import pk.gi.airquality.model.rest.out.CityStations
import pk.gi.airquality.model.rest.out.StationDTO
import pk.gi.airquality.model.rest.out.VoivodeshipCity

@Service
class DataProviderService(
    val sensorDataRepository: SensorDataRepository,
    val airQualityIndexRepository: AirQualityIndexRepository,
    val stationRepository: StationRepository,
    val resultMapper: ResultMapper
) {
    suspend fun getDataForParameterFormula(parameterFormula: String, interval: Number): List<CityStations> {
        if (parameterFormula in FORMULAS || parameterFormula == "all")
            return resultMapper.mapResultListToResponseDataByCity(
                resultMapper.mapEachTupleToDataResult(withContext(Dispatchers.IO) {
                    sensorDataRepository.findAllDataWithStationNameAndParamName(interval)
                }),
                parameterFormula != "all",
                parameterFormula
            ) else {
            throw IllegalFormulaException("Parameter formula $parameterFormula is not supported")
        }
    }

    suspend fun getDataForParameterFormulaByVoivodeship(parameterFormula: String, interval: Number): List<VoivodeshipCity> {
        if (parameterFormula in FORMULAS || parameterFormula == "all")
            return resultMapper.mapResultListToResponseDataByVoivodeship(
                resultMapper.mapEachTupleToDataResult(withContext(Dispatchers.IO) {
                    sensorDataRepository.findAllDataWithStationNameAndParamName(interval)
                }),
                parameterFormula != "all",
                parameterFormula
            )
        else {
            throw IllegalFormulaException("Parameter formula $parameterFormula is not supported")
        }
    }
    suspend fun getAllIndexAfterDate(interval: Number): List<pk.gi.airquality.model.rest.out.AirQualityIndex> {
        return resultMapper.mapDbIndexToRest(withContext(Dispatchers.IO) {
            airQualityIndexRepository.findAllByStCalcDateAfter(interval)
        })
    }

    suspend fun getAllStations(): List<StationDTO> {
        return resultMapper.mapEachTupleToStation(withContext(Dispatchers.IO) {
            stationRepository.findAllStations()
        })
    }

    suspend fun getAverageValues(interval: Number): List<AverageValues> {
        return resultMapper.mapEachTupleToAverageValues(withContext(Dispatchers.IO) {
            sensorDataRepository.findAverageValueForParameter(interval)
        })
    }




    companion object {
        val FORMULAS = listOf("NO2", "O3", "PM10", "PM2.5", "C6H6", "CO", "SO2")
    }
}
