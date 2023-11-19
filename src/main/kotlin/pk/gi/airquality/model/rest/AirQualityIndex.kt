package pk.gi.airquality.model.rest

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime

@JsonIgnoreProperties(ignoreUnknown = true)
data class AirQualityIndex(
    @JsonProperty("id")
    val stationId: Number?,
    @JsonProperty("stCalcDate")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    val stCalcDate: LocalDateTime?,
    @JsonProperty("stIndexLevel")
    val indexLevel: IndexLevel?
)
