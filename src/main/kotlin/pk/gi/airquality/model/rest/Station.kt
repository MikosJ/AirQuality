package pk.gi.airquality.model.rest

import com.fasterxml.jackson.annotation.JsonProperty


data class Station(
    @JsonProperty("id")
    val id: Long,
    @JsonProperty("stationName")
    val name: String,
    @JsonProperty("gegrLat")
    val latitude: Double,
    @JsonProperty("gegrLon")
    val longitude: Double,
    @JsonProperty("city")
    val city: City,
    @JsonProperty("addressStreet")
    val addressStreet: String?
)
