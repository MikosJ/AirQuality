package pk.gi.airquality.db.model

import jakarta.persistence.Entity
import jakarta.persistence.Id

@Entity
data class Station(
    @Id
    val id: Long,
    val name: String,
    val latitude: Double,
    val longitude: Double
)
