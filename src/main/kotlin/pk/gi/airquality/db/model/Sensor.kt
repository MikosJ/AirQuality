package pk.gi.airquality.db.model

import jakarta.persistence.*
import org.hibernate.annotations.Cascade

@Entity
data class Sensor(
    @Id
    val id: Long,
    @ManyToOne(fetch = FetchType.LAZY)
    val station: Station,
)
