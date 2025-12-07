package edu.sfu.controller

import edu.sfu.dto.BookWithDetailsDTO
import edu.sfu.service.BookWithDetailsService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/book-details")
open class BookDetailsController(
    private val bookWithDetailsService: BookWithDetailsService
) {

    @GetMapping
    fun getAllBooksWithDetails(): ResponseEntity<List<BookWithDetailsDTO>> {
        val booksWithDetails = bookWithDetailsService.getBooksWithDetails()
        return ResponseEntity.ok(booksWithDetails)
    }

    @GetMapping("/overdue")
    fun getOverdueBooksWithDetails(): ResponseEntity<List<BookWithDetailsDTO>> {
        val overdueBooks = bookWithDetailsService.getOverdueBooksWithDetails()
        return ResponseEntity.ok(overdueBooks)
    }
}