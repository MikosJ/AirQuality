package pk.gi.airquality.model.rest.out

import com.fasterxml.jackson.annotation.JsonProperty

data class StationData(
    val stationId: Long,
    @JsonProperty("station")
    val stationName: String,
    val latitude: Double,
    val longitude: Double,
    val sensors: List<SensorDataValues>
)
