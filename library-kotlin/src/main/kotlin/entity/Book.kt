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

    // Связь многие-к-одному с автором
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    var author: Author? = null,

    // Связь многие-к-одному с жанром
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "genre_id", nullable = false)
    var genre: Genre? = null,

    // Связь один-ко-многим с выдачами
    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    val bookLoans: MutableList<BookLoan> = mutableListOf()
) {
    constructor() : this(0, "", null, null, null, null, mutableListOf())
    
    override fun toString(): String {
        return "Book(id=$id, title='$title', isbn='$isbn', publicationYear=$publicationYear, authorId=${author?.id}, genreId=${genre?.id})"
    }
}