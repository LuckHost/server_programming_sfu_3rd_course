package edu.sfu.entity

import java.time.LocalDate
import javax.persistence.*

@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "email", nullable = false, unique = true)
    val email: String,

    @Column(name = "first_name", nullable = false)
    val firstName: String,

    @Column(name = "last_name", nullable = false)
    val lastName: String,

    @Column(name = "registration_date", nullable = false)
    val registrationDate: LocalDate
) {
    constructor() : this(0, "", "", "", LocalDate.now())

    override fun toString(): String {
        return "User(id=$id, email='$email', firstName='$firstName', lastName='$lastName', registrationDate=$registrationDate)"
    }
}
