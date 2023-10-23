package pk.gi.airquality.db.model

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
data class SensorData(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    val sensor: Sensor,
    val date: LocalDateTime?,
    @Column(scale = 2)
    val value: BigDecimal,
    @ManyToOne
    val parameter: Parameter
)
