package edu.sfu.dto

data class BookDTO(
    val id: Long? = null,
    val title: String,
    val isbn: String? = null,
    val publicationYear: Int? = null,
    val authorId: Long,
    val authorName: String? = null,
    val genreId: Long,
    val genreName: String? = null
) {
    constructor() : this(null, "", null, null, 0, null, 0, null)
}