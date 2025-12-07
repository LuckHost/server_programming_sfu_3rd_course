package edu.sfu.service

import edu.sfu.dto.BookWithDetailsDTO
import org.hibernate.transform.Transformers
import org.springframework.stereotype.Service
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@Service
open class BookWithDetailsService {

    @PersistenceContext
    private lateinit var entityManager: EntityManager

    fun getBooksWithDetails(): List<BookWithDetailsDTO> {
        val sql = """
            SELECT 
                b.id as bookId,
                b.title as bookTitle,
                b.publication_year as publicationYear,
                a.id as authorId,
                a.name as authorName,
                a.country as authorCountry,
                g.id as genreId,
                g.name as genreName,
                COUNT(bl.id) as loanCount,
                CASE 
                    WHEN EXISTS (
                        SELECT 1 FROM book_loans bl2 
                        WHERE bl2.book_id = b.id 
                        AND bl2.return_date IS NULL 
                        AND bl2.due_date < CURRENT_DATE
                    ) THEN true 
                    ELSE false 
                END as isOverdue
            FROM books b
            JOIN authors a ON b.author_id = a.id
            JOIN genres g ON b.genre_id = g.id
            LEFT JOIN book_loans bl ON b.id = bl.book_id
            GROUP BY b.id, a.id, g.id
            ORDER BY b.title
        """.trimIndent()

        val query = entityManager.createNativeQuery(sql)

        // Используем Transformers для маппинга результатов в DTO
        val result = query.unwrap(org.hibernate.query.NativeQuery::class.java)
            .setResultTransformer(Transformers.aliasToBean(BookWithDetailsDTO::class.java))
            .resultList

        return result as List<BookWithDetailsDTO>
    }

    fun getOverdueBooksWithDetails(): List<BookWithDetailsDTO> {
        val sql = """
            SELECT 
                b.id as bookId,
                b.title as bookTitle,
                b.publication_year as publicationYear,
                a.id as authorId,
                a.name as authorName,
                a.country as authorCountry,
                g.id as genreId,
                g.name as genreName,
                COUNT(bl.id) as loanCount,
                true as isOverdue
            FROM books b
            JOIN authors a ON b.author_id = a.id
            JOIN genres g ON b.genre_id = g.id
            JOIN book_loans bl ON b.id = bl.book_id
            WHERE bl.return_date IS NULL 
            AND bl.due_date < CURRENT_DATE
            GROUP BY b.id, a.id, g.id
            ORDER BY b.title
        """.trimIndent()

        val query = entityManager.createNativeQuery(sql)

        val result = query.unwrap(org.hibernate.query.NativeQuery::class.java)
            .setResultTransformer(Transformers.aliasToBean(BookWithDetailsDTO::class.java))
            .resultList

        return result as List<BookWithDetailsDTO>
    }
}