package pk.gi.airquality.model.rest

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime

data class Values(
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @JsonProperty("date")
        val date: LocalDateTime?,
        @JsonProperty("value")
        val value: Double?
)
