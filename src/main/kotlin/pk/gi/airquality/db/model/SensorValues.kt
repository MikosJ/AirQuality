package pk.gi.airquality.db.model

import com.fasterxml.jackson.annotation.JsonFormat
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
data class SensorValues(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    val date: LocalDateTime,
    val value: Double,
    @OneToOne
    val sensorId: Sensor
)
