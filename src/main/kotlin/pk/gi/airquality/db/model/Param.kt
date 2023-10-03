package pk.gi.airquality.db.model

import jakarta.persistence.*

@Entity
data class Param(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long,
    val name: String,
    val formula: String,
    val code: String
)
