package pk.gi.airquality.db.model

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import jakarta.persistence.JoinColumn


@Entity
data class Station(
    @Id
    val stationId: Long? = null,
    val stationName: String,
    val gegrLat: Double,
    val gegrLon: Double,

    @ManyToOne
    @JoinColumn(name = "city_id")
    val city: City?,

    val addressStreet: String?
)
