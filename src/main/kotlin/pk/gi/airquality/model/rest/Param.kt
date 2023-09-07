package pk.gi.airquality.model.rest

import com.fasterxml.jackson.annotation.JsonProperty

data class Param(
        @JsonProperty("paramName")
        val name: String,
        @JsonProperty("paramFormula")
        val formula: String,
        @JsonProperty("paramCode")
        val code: String,
        @JsonProperty("idParam")
        val id: Long
)
