package pk.gi.airquality.model.rest

import com.fasterxml.jackson.annotation.JsonProperty

data class City(
    val id: Long,
    val name: String,
    val commune: Commune
)
