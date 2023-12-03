package pk.gi.airquality.model.rest.out

import java.time.LocalDateTime

data class GraphValues(
    val value: Number,
    val date: LocalDateTime,
    val parameterFormula: String,
    val parameterName: String,
    val stationName: String,
    val city: String,
    val voivodeship: String,
    val longitude: Number,
    val latitude: Number,
    val stationId: String
)
