package pk.gi.airquality.model.rest.out

data class Parameter(val name: String, val formula: String, val values: List<SensorValues>)
