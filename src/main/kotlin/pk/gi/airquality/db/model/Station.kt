package pk.gi.airquality.db.model

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.FetchType


@Entity
data class Station(
    @Id
    val stationId: Long,
    val stationName: String,
    val gegrLat: Double,
    val gegrLon: Double,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id")
    val city: City?,

    val addressStreet: String?
)
