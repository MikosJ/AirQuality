package pk.gi.airquality.db.model

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import jakarta.persistence.FetchType


@Entity
data class Sensor(
    @Id
    val id: Long,
    @ManyToOne(fetch = FetchType.LAZY)
    val station: Station,
)
