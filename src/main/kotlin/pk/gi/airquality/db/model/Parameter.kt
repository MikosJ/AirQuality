package pk.gi.airquality.db.model

import jakarta.persistence.*

@Entity
data class Parameter(
    val paramName: String,
    val paramFormula: String,
    val paramCode: String,
    val idParam: Long,
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val paramId: Long? = null
)
