package repository

import edu.sfu.entity.BookLoan
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
interface BookLoanRepository : JpaRepository<BookLoan, Long> {
    
    fun findByReturnDateIsNullAndDueDateBefore(dueDate: LocalDate): List<BookLoan>
    
    fun findByBookIdAndReturnDateIsNull(bookId: Long): List<BookLoan>
    
    fun findByUserId(userId: Long): List<BookLoan>
}