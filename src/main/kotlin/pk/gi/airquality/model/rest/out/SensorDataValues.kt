package pk.gi.airquality.model.rest.out

data class SensorDataValues(
    val sensorId: Long,
    val parameterName: String,
    val parameterFormula: String,
    val values: List<SensorValues>
)
