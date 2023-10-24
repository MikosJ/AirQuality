package pk.gi.airquality.model.rest

import com.fasterxml.jackson.annotation.JsonProperty

data class Commune(
    @JsonProperty("communeName")
    val name: String,
    @JsonProperty("districtName")
    val district: String,
    @JsonProperty("provinceName")
    val province: String
)
