package edu.sfu.repository

import edu.sfu.entity.Book
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
interface BookRepository : JpaRepository<Book, Long> {

    fun findByTitleContainingIgnoreCase(title: String): List<Book>

    fun findByPublicationYearBetween(startYear: Int, endYear: Int): List<Book>

    @Query("""
        SELECT b FROM Book b 
        JOIN b.author a 
        WHERE a.name LIKE %:authorName%
    """)
    fun findByAuthorName(@Param("authorName") authorName: String): List<Book>

    @Query("""
        SELECT b FROM Book b 
        JOIN b.genre g 
        WHERE g.name = :genreName
    """)
    fun findByGenreName(@Param("genreName") genreName: String): List<Book>

    @Query("""
        SELECT b FROM Book b 
        JOIN b.bookLoans bl 
        WHERE bl.returnDate IS NULL 
        AND bl.dueDate < CURRENT_DATE
    """)
    fun findOverdueBooks(): List<Book>
}