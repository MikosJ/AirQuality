package pk.gi.airquality.model.rest.out

data class AverageValue(
    val value: Number,
    val parameterFormula: String,
    val parameterName: String,
    val voivodeship: String
)
