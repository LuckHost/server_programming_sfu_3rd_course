package edu.sfu.entity

import javax.persistence.*

@Entity
@Table(name = "books")
data class Book(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "title", nullable = false)
    val title: String,

    @Column(name = "isbn")
    val isbn: String? = null,

    @Column(name = "publication_year")
    val publicationYear: Int? = null,

    @Column(name = "author_id", nullable = false)
    val authorId: Long,

    @Column(name = "genre_id", nullable = false)
    val genreId: Long
) {
    constructor() : this(0, "", null, null, 0, 0)

    override fun toString(): String {
        return "Book(id=$id, title='$title', isbn='$isbn', publicationYear=$publicationYear, authorId=$authorId, genreId=$genreId)"
    }
}
