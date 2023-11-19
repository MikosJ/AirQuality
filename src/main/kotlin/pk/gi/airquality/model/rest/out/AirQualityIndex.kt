package pk.gi.airquality.model.rest.out

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime

data class AirQualityIndex(val stationId: Number?,
                           @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                           val stCalcDate: LocalDateTime?, val indexLevel: IndexLevel?)
