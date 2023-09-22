package pk.gi.airquality.db.model

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.OneToMany

@Entity
data class Param(
    @Id val id: Long,
    val name: String,
    val formula: String,
    val code: String,
    @OneToMany
    val values: List<SensorValues>
)
