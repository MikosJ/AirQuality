package pk.gi.airquality.db.model

import jakarta.persistence.*

@Entity
data class Sensor(
    @Id
    val id: Long,
    @OneToOne
    val parameter: Param,
    @OneToOne
    val station: Station
)
