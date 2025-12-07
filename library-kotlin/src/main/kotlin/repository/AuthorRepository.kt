package edu.sfu.repository

import edu.sfu.entity.Author
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface AuthorRepository : JpaRepository<Author, Long> {

    fun findByNameContainingIgnoreCase(name: String): List<Author>

    fun findByCountry(country: String): List<Author>

    @Query("SELECT a FROM Author a WHERE SIZE(a.books) >= :minBooks")
    fun findAuthorsWithMinBooks(@Param("minBooks") minBooks: Int): List<Author>
}