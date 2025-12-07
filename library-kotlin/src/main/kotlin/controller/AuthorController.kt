package controller

import edu.sfu.dto.AuthorCreateDTO
import edu.sfu.dto.AuthorDTO
import edu.sfu.dto.AuthorUpdateDTO
import edu.sfu.entity.Author
import edu.sfu.service.AuthorService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
@RequestMapping("/api/authors")
open class AuthorController(
    private val authorService: AuthorService
) {
    
    @GetMapping
    fun getAllAuthors(
        @RequestParam(required = false) name: String?,
        @RequestParam(required = false) country: String?
    ): ResponseEntity<List<AuthorDTO>> {
        val authors = authorService.searchAuthors(name, country)
        val authorDTOs = authors.map { it.toDTO() }
        return ResponseEntity.ok(authorDTOs)
    }
    
    @GetMapping("/{id}")
    fun getAuthorById(@PathVariable id: Long): ResponseEntity<AuthorDTO> {
        return authorService.getAuthorById(id)
            ?.let { ResponseEntity.ok(it.toDTO()) }
            ?: ResponseEntity.notFound().build()
    }
    
    @PostMapping
    fun createAuthor(@RequestBody authorCreateDTO: AuthorCreateDTO): ResponseEntity<AuthorDTO> {
        val author = Author(
            name = authorCreateDTO.name,
            birthDate = authorCreateDTO.birthDate,
            country = authorCreateDTO.country
        )
        val savedAuthor = authorService.createAuthor(author)
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAuthor.toDTO())
    }
    
    @PutMapping("/{id}")
    fun updateAuthor(
        @PathVariable id: Long,
        @RequestBody authorUpdateDTO: AuthorUpdateDTO
    ): ResponseEntity<AuthorDTO> {
        val author = Author(
            name = authorUpdateDTO.name ?: "",
            birthDate = authorUpdateDTO.birthDate,
            country = authorUpdateDTO.country
        )
        return authorService.updateAuthor(id, author)
            ?.let { ResponseEntity.ok(it.toDTO()) }
            ?: ResponseEntity.notFound().build()
    }
    
    @DeleteMapping("/{id}")
    fun deleteAuthor(@PathVariable id: Long): ResponseEntity<Void> {
        return if (authorService.deleteAuthor(id)) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }
    
    // Helper function to convert Entity to DTO
    private fun Author.toDTO(): AuthorDTO = AuthorDTO(
        id = this.id,
        name = this.name,
        birthDate = this.birthDate,
        country = this.country
    )
}