package pk.gi.airquality.model.rest.out

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime

data class GraphAverageVoivodeshipValue(
    val averageValue: Number,
    @JsonFormat(pattern="yyyy-MM-dd HH:mm")
    val date: LocalDateTime,
    val parameterFormula: String,
    val parameterName: String,
    val voivodeship: String
)