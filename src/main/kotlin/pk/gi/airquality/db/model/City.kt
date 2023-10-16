package pk.gi.airquality.db.model

import jakarta.persistence.Entity
import jakarta.persistence.Id

@Entity
data class City(
    @Id
    val id: Long,
    val name: String,
    val communeName: String,
    val districtName: String,
    val provinceName: String
)