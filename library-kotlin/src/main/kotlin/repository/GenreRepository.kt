package repository

import edu.sfu.entity.Genre
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface GenreRepository : JpaRepository<Genre, Long> {
    
    fun findByNameContainingIgnoreCase(name: String): List<Genre>
    
    fun findByName(name: String): Genre?
}