package pk.gi.airquality.model.rest

data class Sensor(
        val id: Long,
        val stationId: Long,
        val param: Param
)
