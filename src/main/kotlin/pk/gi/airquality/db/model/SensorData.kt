package pk.gi.airquality.db.model

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import jakarta.persistence.JoinColumn
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import java.time.LocalDateTime

@Entity
data class SensorData(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val dataId: Long? = null,

    @ManyToOne
    @JoinColumn(name = "sensor_id")
    val sensor: Sensor,

    val date: LocalDateTime?,
    val value: Double?,
    @ManyToOne
    @JoinColumn(name = "param_id")
    val parameter: Parameter
)
