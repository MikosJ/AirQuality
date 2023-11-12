package pk.gi.airquality.service

import com.fasterxml.jackson.annotation.JsonFormat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import org.springframework.stereotype.Service
import pk.gi.airquality.db.service.SensorDataRepository
import pk.gi.airquality.db.service.StationRepository
import pk.gi.airquality.model.rest.out.SensorDataValues
import pk.gi.airquality.model.rest.out.SensorValues
import pk.gi.airquality.model.rest.out.StationData
import java.math.BigDecimal
import java.time.LocalDateTime

@Service
class DataProviderService(
    val sensorDataRepository: SensorDataRepository,
    val stationRepository: StationRepository
) {
    private suspend fun fetchSensorDataForStationsSince(cutoffDate: LocalDateTime): List<StationData> {
        return withContext(Dispatchers.IO) {
            val stations = stationRepository.findAll()

            val sensorDataList = stations.map { station ->
                async {
                    val sensorData =
                        sensorDataRepository.findAllBySensorStationStationIdAndDateAfter(station.stationId, cutoffDate)
                    val sensorDataMap = sensorData.groupBy { it.sensor.id to (it.parameterFormula to it.parameterName) }

                    val sensorDataValues = sensorDataMap.map { (sensorParamId, data) ->
                        val (sensorId, parameter) = sensorParamId
                        val sensorValues = data.map {
                            SensorValues(it.date!!, it.value)
                        }
                        SensorDataValues(sensorId, parameter.second, parameter.first, sensorValues)
                    }

                    StationData(
                        station.stationId,
                        station.stationName,
                        station.gegrLat,
                        station.gegrLon,
                        sensorDataValues
                    )
                }
            }

            sensorDataList.awaitAll()
        }
    }

    suspend fun getLast3DaysData(): List<StationData> {
        val cutoffDate = LocalDateTime.now().minusDays(THREE)
        return fetchSensorDataForStationsSince(cutoffDate)
    }

    suspend fun getLastAddedSensorData(): List<StationData> {
        val lastFourHours = LocalDateTime.now().minusHours(ONE)
        return fetchSensorDataForStationsSince(lastFourHours)
    }

    fun test(): List<ResultProjection> {
        return sensorDataRepository.findAllDataWithStationNameAndParamName()
    }
    interface ResultProjection {
        val station_name: String

        val station_city: String

        val parameter_formula: String

        val parameter_name: String

        val value: BigDecimal

        @get:JsonFormat(pattern = "dd.MM.YYYY-HH:mm")
        val date: LocalDateTime
    }



    companion object {
        const val THREE = 3L
        const val ONE = 1L
    }
}
