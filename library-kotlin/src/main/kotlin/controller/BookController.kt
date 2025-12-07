package edu.sfu.controller

import edu.sfu.dto.BookDTO
import edu.sfu.entity.Book
import edu.sfu.repository.AuthorRepository
import edu.sfu.repository.BookRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import repository.GenreRepository
import javax.persistence.EntityNotFoundException
@RestController
@RequestMapping("/api/books")
open class BookController(
    private val bookRepository: BookRepository
) {

    @GetMapping
    fun getAllBooks(): ResponseEntity<List<BookDTO>> {
        val books = bookRepository.findAll()
        val bookDTOs = books.map { it.toDTO() }
        return ResponseEntity.ok(bookDTOs)
    }

    @GetMapping("/{id}")
    fun getBookById(@PathVariable id: Long): ResponseEntity<BookDTO> {
        return bookRepository.findById(id)
            .map { ResponseEntity.ok(it.toDTO()) }
            .orElse(ResponseEntity.notFound().build())
    }

    @GetMapping("/overdue")
    fun getOverdueBooks(): ResponseEntity<List<BookDTO>> {
        val overdueBooks = bookRepository.findOverdueBooks()
        return ResponseEntity.ok(overdueBooks.map { it.toDTO() })
    }

    @DeleteMapping("/{id}")
    fun deleteBook(@PathVariable id: Long): ResponseEntity<Void> {
        return if (bookRepository.existsById(id)) {
            bookRepository.deleteById(id)
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }

    // Helper function to convert Entity to DTO
    private fun Book.toDTO(): BookDTO = BookDTO(
        id = this.id,
        title = this.title,
        isbn = this.isbn,
        publicationYear = this.publicationYear,
        authorId = this.author?.id ?: 0,
        authorName = this.author?.name,
        genreId = this.genre?.id ?: 0,
        genreName = this.genre?.name
    )
}