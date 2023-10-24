package pk.gi.airquality.model.rest


data class City(
    val id: Long,
    val name: String,
    val commune: Commune
)
