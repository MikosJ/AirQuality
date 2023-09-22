package pk.gi.airquality.db.model

import com.fasterxml.jackson.annotation.JsonFormat
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.OneToOne
import java.time.LocalDateTime

@Entity
data class SensorValues(
    @Id val id: Long,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    val date: LocalDateTime,
    val value: Double,
    @OneToOne
    val sensorId: Sensor
)
