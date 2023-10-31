package pk.gi.airquality.model.rest.out

import com.fasterxml.jackson.annotation.JsonFormat
import java.math.BigDecimal
import java.time.LocalDateTime

data class SensorValues(
    @JsonFormat(pattern="yyyy-MM-dd HH:mm")
    val date: LocalDateTime,
    val value: BigDecimal
)
