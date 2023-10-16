package pk.gi.airquality.db.model

import jakarta.persistence.*

@Entity
data class Station(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val stationId: Long? = null,
    val stationName: String,
    val gegrLat: Double,
    val gegrLon: Double,

    @ManyToOne
    @JoinColumn(name = "city_id")
    val city: City?,

    val addressStreet: String?
)
