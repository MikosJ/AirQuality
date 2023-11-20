package pk.gi.airquality.model.rest.out

import com.fasterxml.jackson.annotation.JsonFormat
import java.math.BigDecimal
import java.time.LocalDateTime

data class Result(
    val value: BigDecimal,
    @JsonFormat(pattern="yyyy-MM-dd HH:mm")
    val date: LocalDateTime,
    val parameterFormula: String,
    val parameterName: String,
    val stationName: String,
    val city: String,
    val voivodeship: String,
    val longitude: Number,
    val latitude: Number,
    val stationId: Number
)
