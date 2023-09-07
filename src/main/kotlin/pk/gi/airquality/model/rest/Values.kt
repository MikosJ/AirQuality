package pk.gi.airquality.model.rest

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime

data class Values(
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        val date: LocalDateTime,
        val value: Double
)
