package pk.gi.airquality.model.rest

import com.fasterxml.jackson.annotation.JsonProperty


data class Station(
    val id: Long,
    @JsonProperty("stationName")
    val name: String,
    @JsonProperty("gegrLat")
    val latitude: Double,
    @JsonProperty("gegrLon")
    val longitude: Double
)
