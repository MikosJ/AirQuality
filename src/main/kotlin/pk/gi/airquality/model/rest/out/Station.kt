package pk.gi.airquality.model.rest.out

data class Station(val name: String, val id: Number, val longitude: Number, val latitude: Number, val parameter: List<Parameter>)
