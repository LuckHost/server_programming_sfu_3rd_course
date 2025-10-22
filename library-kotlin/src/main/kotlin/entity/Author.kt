package edu.sfu.entity

import java.time.LocalDate
import javax.persistence.*

@Entity
@Table(name = "authors")
data class Author(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "name", nullable = false)
    val name: String,

    @Column(name = "birth_date")
    val birthDate: LocalDate? = null,

    @Column(name = "country")
    val country: String? = null
) {
    constructor() : this(0, "", null, null)

    override fun toString(): String {
        return "Author(id=$id, name='$name', birthDate=$birthDate, country='$country')"
    }
}
