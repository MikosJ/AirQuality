package pk.gi.airquality.db.model

import jakarta.persistence.Entity
import jakarta.persistence.Id

@Entity
data class Parameter(
    val paramName: String,
    val paramFormula: String,
    val paramCode: String,
    val idParam: Long,
    @Id
    val paramId: Long? = null
)
