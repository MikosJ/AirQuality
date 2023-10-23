package pk.gi.airquality.db.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToOne

@Entity
data class Parameter(
    val paramName: String,
    val paramFormula: String,
    val paramCode: String,
    val idParam: Long,
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val paramId: Long? = null
)
