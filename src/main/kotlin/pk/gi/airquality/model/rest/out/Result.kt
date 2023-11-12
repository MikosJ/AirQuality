package pk.gi.airquality.model.rest.out

import com.fasterxml.jackson.annotation.JsonProperty
import java.math.BigDecimal
import java.time.LocalDateTime

data class Result(
    @JsonProperty("station_name")
    val stationName: String,
    @JsonProperty("station_city")
    val city: String,
    @JsonProperty("parameter_formula")
    val parameterFormula: String,
    @JsonProperty("parameter_name")
    val parameterName: String,
    @JsonProperty("value")
    val value: BigDecimal,
    @JsonProperty("date")
    val date: LocalDateTime
)