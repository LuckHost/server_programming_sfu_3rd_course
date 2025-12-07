package edu.sfu.service

import edu.sfu.entity.Author
import edu.sfu.repository.AuthorRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
open class AuthorService(
    private val authorRepository: AuthorRepository
) {

    fun getAllAuthors(): List<Author> = authorRepository.findAll()

    fun getAuthorById(id: Long): Author? = authorRepository.findById(id).orElse(null)

    fun createAuthor(author: Author): Author = authorRepository.save(author)

    fun updateAuthor(id: Long, authorData: Author): Author? {
        return authorRepository.findById(id).map { existingAuthor ->
            // Создаем новый объект с обновленными полями
            val updatedAuthor = Author(
                id = existingAuthor.id,
                name = authorData.name.takeIf { it.isNotBlank() } ?: existingAuthor.name,
                birthDate = authorData.birthDate ?: existingAuthor.birthDate,
                country = authorData.country ?: existingAuthor.country,
                books = existingAuthor.books // Сохраняем существующие книги
            )
            authorRepository.save(updatedAuthor)
        }.orElse(null)
    }

    fun deleteAuthor(id: Long): Boolean {
        return if (authorRepository.existsById(id)) {
            authorRepository.deleteById(id)
            true
        } else {
            false
        }
    }

    fun searchAuthors(name: String? = null, country: String? = null): List<Author> {
        return when {
            !name.isNullOrBlank() && !country.isNullOrBlank() -> {
                authorRepository.findByNameContainingIgnoreCase(name)
                    .filter { it.country.equals(country, ignoreCase = true) }
            }
            !name.isNullOrBlank() -> authorRepository.findByNameContainingIgnoreCase(name)
            !country.isNullOrBlank() -> authorRepository.findByCountry(country)
            else -> authorRepository.findAll()
        }
    }
}