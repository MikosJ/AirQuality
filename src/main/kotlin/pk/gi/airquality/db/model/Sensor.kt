package pk.gi.airquality.db.model

import jakarta.persistence.*

@Entity
data class Sensor(
    @Id
    val id: Long,
    @ManyToMany
    @JoinColumn(name="parameter_id")
    val parameter: List<Parameter>

)
