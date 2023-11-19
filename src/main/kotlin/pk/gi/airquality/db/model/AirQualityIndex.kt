package pk.gi.airquality.db.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
data class AirQualityIndex(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null,
    @ManyToOne
    val stationId: Station?,
    val stCalcDate: LocalDateTime?,
    val indexLevelId: Long?,
    val indexLevelName: String?
)