package pk.gi.airquality.model.rest

import com.fasterxml.jackson.annotation.JsonProperty

data class SensorData(
        @JsonProperty("key")
        val key: String,
        @JsonProperty("values")
        val values: List<Values>
)
