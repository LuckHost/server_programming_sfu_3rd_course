package edu.sfu.dto

import java.time.LocalDate

data class AuthorDTO(
    val id: Long? = null,
    val name: String,
    val birthDate: LocalDate? = null,
    val country: String? = null
) {
    constructor() : this(null, "", null, null)
}

data class AuthorCreateDTO(
    val name: String,
    val birthDate: LocalDate? = null,
    val country: String? = null
)

data class AuthorUpdateDTO(
    val name: String? = null,
    val birthDate: LocalDate? = null,
    val country: String? = null
)