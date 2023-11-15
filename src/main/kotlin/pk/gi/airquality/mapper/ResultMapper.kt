package pk.gi.airquality.mapper

import jakarta.persistence.Tuple
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import org.springframework.stereotype.Component
import pk.gi.airquality.model.rest.out.*
import java.math.BigDecimal
import java.sql.Timestamp

@Component
class ResultMapper {
    suspend fun mapEachTupleToResult(list: List<Tuple>): List<Result> {
        return withContext(Dispatchers.IO) {
            val resultList = list.map { tuple ->
                async {
                    Result(
                        tuple.get(0, BigDecimal::class.java),
                        tuple.get(1, Timestamp::class.java).toLocalDateTime(),
                        tuple.get(2, String::class.java),
                        tuple.get(3, String::class.java),
                        tuple.get(4, String::class.java),
                        tuple.get(5, String::class.java),
                        tuple.get(6, String::class.java),
                        tuple.get(7, Number::class.java),
                        tuple.get(8, Number::class.java)
                    )
                }
            }
            resultList.awaitAll()
        }
    }

    fun mapSensorDataToParameters(
        stationSensorData: List<Result>,
        isParameterSpecified: Boolean,
        parameterFormula: String?
    ): List<Parameter> {
        val groupedByParameter = stationSensorData.groupBy { it.parameterFormula }
        return if (isParameterSpecified) {
            groupedByParameter
                .filter { it.key == parameterFormula }
                .map { (_, parameterSensorData) ->
                    val sensorValues = parameterSensorData.map {
                        SensorValues(it.date, it.value)
                    }
                    Parameter(parameterSensorData[0].parameterName, parameterFormula!!, sensorValues)
                }
        } else {
            groupedByParameter.flatMap { (paramFormula, parameterSensorData) ->
                val sensorValues = parameterSensorData.map {
                    SensorValues(it.date, it.value)
                }
                listOf(
                    Parameter(parameterSensorData[0].parameterName, paramFormula, sensorValues)
                )
            }
        }
    }

    fun mapResultListToResponseDataByCity(
        list: List<Result>,
        isParameterSpecified: Boolean,
        parameterFormula: String?
    ): List<CityStations> {
        val groupedByCity = list.groupBy { it.city }
        return groupedByCity.map { (city, citySensorData) ->
            val groupedByStation = citySensorData.groupBy { it.stationName }
            val stations = groupedByStation.map { (station, stationSensorData) ->
                val parameters = mapSensorDataToParameters(stationSensorData, isParameterSpecified, parameterFormula)
                val firstStation = stationSensorData[0]
                Station(
                    firstStation.stationName,
                    firstStation.longitude,
                    firstStation.latitude,
                    parameters
                )
            }
            CityStations(city, stations)
        }
    }

    fun mapResultListToResponseDataByVoivodeship(
        list: List<Result>,
        isParameterSpecified: Boolean,
        parameterFormula: String?
    ): List<VoivodeshipCity> {
        val groupedByVoivodeship = list.groupBy { it.voivodeship }
        return groupedByVoivodeship.map { (voivodeship, citySensorData) ->
            val groupedByCity = citySensorData.groupBy { it.city }
            val cities = groupedByCity.map { (city, station) ->
                val groupedByStation = station.groupBy { it.stationName }
                val stations = groupedByStation.map { (station, stationSensorData) ->
                    val parameters = mapSensorDataToParameters(stationSensorData, isParameterSpecified, parameterFormula)
                    val firstStation = stationSensorData[0]
                    Station(
                        firstStation.stationName,
                        firstStation.longitude,
                        firstStation.latitude,
                        parameters
                    )
                }
                CityStations(city, stations)
            }
            VoivodeshipCity(voivodeship, cities)
        }
    }


}