package edu.sfu.entity

import java.time.LocalDate
import javax.persistence.*

@Entity
@Table(name = "authors")
data class Author(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "name", nullable = false)
    val name: String,

    @Column(name = "birth_date")
    val birthDate: LocalDate? = null,

    @Column(name = "country")
    val country: String? = null,

    // Связь один-ко-многим с книгами - УБИРАЕМ ИЗ toString
    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    val books: MutableList<Book> = mutableListOf()
) {
    constructor() : this(0, "", null, null, mutableListOf())

    fun addBook(book: Book) {
        books.add(book)
        book.author = this
    }

    override fun toString(): String {
        return "Author(id=$id, name='$name', birthDate=$birthDate, country='$country')"
        // Убрали booksCount из toString чтобы избежать LazyInitializationException
    }

    // Дополнительный метод для безопасного получения количества книг
    fun getBooksCountSafe(): Int {
        return try {
            books.size
        } catch (e: Exception) {
            0 // Возвращаем 0 если коллекция не инициализирована
        }
    }
}