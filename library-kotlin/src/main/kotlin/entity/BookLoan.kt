package edu.sfu.entity

import java.time.LocalDate
import javax.persistence.*

@Entity
@Table(name = "book_loans")
data class BookLoan(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    // Связь многие-к-одному с книгой
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    var book: Book? = null,

    // Связь многие-к-одному с пользователем
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var user: User? = null,

    @Column(name = "loan_date", nullable = false)
    val loanDate: LocalDate,

    @Column(name = "due_date", nullable = false)
    val dueDate: LocalDate,

    @Column(name = "return_date")
    val returnDate: LocalDate? = null
) {
    constructor() : this(0, null, null, LocalDate.now(), LocalDate.now().plusDays(30), null)
    
    override fun toString(): String {
        return "BookLoan(id=$id, bookId=${book?.id}, userId=${user?.id}, loanDate=$loanDate, dueDate=$dueDate, returnDate=$returnDate)"
    }
}