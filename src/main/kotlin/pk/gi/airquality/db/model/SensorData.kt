package pk.gi.airquality.db.model

import jakarta.persistence.*

@Entity
data class SensorData(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val dataId: Long? = null,

    @ManyToOne
    @JoinColumn(name = "sensor_id")
    val sensor: Sensor,

    val date: String,
    val value: Double
)
