package edu.sfu.entity

import javax.persistence.*

@Entity
@Table(name = "genres")
data class Genre(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "name", nullable = false, unique = true)
    val name: String,

    @Column(name = "description")
    val description: String? = null
) {
    constructor() : this(0, "", null)

    override fun toString(): String {
        return "Genre(id=$id, name='$name', description='$description')"
    }
}
