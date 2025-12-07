package edu.sfu.dto

import java.time.LocalDate

// Кастомная модель, объединяющая данные из нескольких таблиц
data class BookWithDetailsDTO(
    val bookId: Long,
    val bookTitle: String,
    val publicationYear: Int?,
    val authorId: Long,
    val authorName: String,
    val authorCountry: String?,
    val genreId: Long,
    val genreName: String,
    val isOverdue: Boolean = false,
    val loanCount: Long = 0
) {
    constructor() : this(0, "", null, 0, "", null, 0, "", false, 0)
}