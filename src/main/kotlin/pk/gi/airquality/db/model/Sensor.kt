package pk.gi.airquality.db.model

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.OneToOne

@Entity
data class Sensor(
    @Id val id: Long,
    @OneToOne
    val stationId: Station,
    @OneToOne
    val parameter: Param
)
