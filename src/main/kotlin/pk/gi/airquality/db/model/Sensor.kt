package pk.gi.airquality.db.model

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToMany

@Entity
data class Sensor(
    @Id
    val id: Long,
    @ManyToMany
    @JoinColumn(name="parameter_id")
    val parameter: List<Parameter>

)
