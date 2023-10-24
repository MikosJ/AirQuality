package pk.gi.airquality.db.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Column
import jakarta.persistence.ManyToOne
import jakarta.persistence.FetchType
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
