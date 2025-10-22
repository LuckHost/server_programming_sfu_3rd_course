package edu.sfu.entity

import java.time.LocalDate
import javax.persistence.*

@Entity
@Table(name = "book_loans")
data class BookLoan(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "book_id", nullable = false)
    val bookId: Long,

    @Column(name = "user_id", nullable = false)
    val userId: Long,

    @Column(name = "loan_date", nullable = false)
    val loanDate: LocalDate,

    @Column(name = "due_date", nullable = false)
    val dueDate: LocalDate,

    @Column(name = "return_date")
    val returnDate: LocalDate? = null
) {
    constructor() : this(0, 0, 0, LocalDate.now(), LocalDate.now(), null)

    override fun toString(): String {
        return "BookLoan(id=$id, bookId=$bookId, userId=$userId, loanDate=$loanDate, dueDate=$dueDate, returnDate=$returnDate)"
    }
}