package pk.gi.airquality.model.rest

import com.fasterxml.jackson.annotation.JsonProperty

data class IndexLevel(
    @JsonProperty("id")
    val id: Long?,
    @JsonProperty("indexLevelName")
    val indexLevelName: String?
)
