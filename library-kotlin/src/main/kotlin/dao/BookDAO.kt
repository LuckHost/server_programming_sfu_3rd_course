package edu.sfu.dao

import edu.sfu.entity.Book
import org.hibernate.Session
import java.time.LocalDate
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Join
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root

class BookDAO : BaseDAO<Book>() {
    override fun getEntityClass(): Class<Book> = Book::class.java

    // Criteria API запрос из ПЗ №3 (пункт 5 задания) - ИСПРАВЛЕННАЯ ВЕРСИЯ
    fun findOverdueBooks(genreName: String? = "Фантастика", minPublicationYear: Int? = 2000): List<Book> {
        val session = getSession()
        return try {
            val criteriaBuilder: CriteriaBuilder = session.criteriaBuilder
            val criteriaQuery: CriteriaQuery<Book> = criteriaBuilder.createQuery(Book::class.java)
            val bookRoot: Root<Book> = criteriaQuery.from(Book::class.java)

            // Join с другими таблицами
            val genreJoin: Join<Book, Any> = bookRoot.join<Book, Any>("genre")
            val authorJoin: Join<Book, Any> = bookRoot.join<Book, Any>("author")
            val loanJoin: Join<Book, Any> = bookRoot.join<Book, Any>("bookLoans")

            val predicates = mutableListOf<Predicate>()

            // Условия из ПЗ №3
            predicates.add(criteriaBuilder.isNull(loanJoin.get<Any>("returnDate")))
            predicates.add(criteriaBuilder.lessThan(loanJoin.get<LocalDate>("dueDate"), LocalDate.now()))

            if (!genreName.isNullOrBlank()) {
                predicates.add(criteriaBuilder.equal(genreJoin.get<String>("name"), genreName))
            }

            if (minPublicationYear != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(bookRoot.get<Int>("publicationYear"), minPublicationYear))
            }

            // Исправляем условие просрочки более 30 дней
            // Вместо diff используем более простой подход
            val thirtyDaysAgo = LocalDate.now().minusDays(30)
            predicates.add(criteriaBuilder.lessThan(loanJoin.get<LocalDate>("dueDate"), thirtyDaysAgo))

            criteriaQuery.select(bookRoot)
            criteriaQuery.where(*predicates.toTypedArray())
            criteriaQuery.distinct(true)

            session.createQuery(criteriaQuery).resultList
        } finally {
            closeSession()
        }
    }

    // Альтернативная версия с использованием функций даты
    fun findOverdueBooksAlternative(genreName: String? = "Фантастика", minPublicationYear: Int? = 2000): List<Book> {
        val session = getSession()
        return try {
            val criteriaBuilder: CriteriaBuilder = session.criteriaBuilder
            val criteriaQuery: CriteriaQuery<Book> = criteriaBuilder.createQuery(Book::class.java)
            val bookRoot: Root<Book> = criteriaQuery.from(Book::class.java)

            val genreJoin: Join<Book, Any> = bookRoot.join<Book, Any>("genre")
            val loanJoin: Join<Book, Any> = bookRoot.join<Book, Any>("bookLoans")

            val predicates = mutableListOf<Predicate>()

            // Основные условия
            predicates.add(criteriaBuilder.isNull(loanJoin.get<Any>("returnDate")))
            predicates.add(criteriaBuilder.lessThan(loanJoin.get<LocalDate>("dueDate"), LocalDate.now()))

            if (!genreName.isNullOrBlank()) {
                predicates.add(criteriaBuilder.equal(genreJoin.get<String>("name"), genreName))
            }

            if (minPublicationYear != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(bookRoot.get<Int>("publicationYear"), minPublicationYear))
            }

            // Для подсчета дней просрочки можно использовать функцию базы данных
            // Но для простоты оставим условие что due_date был больше 30 дней назад
            val monthAgo = LocalDate.now().minusMonths(1)
            predicates.add(criteriaBuilder.lessThan(loanJoin.get<LocalDate>("dueDate"), monthAgo))

            criteriaQuery.select(bookRoot)
            criteriaQuery.where(*predicates.toTypedArray())
            criteriaQuery.distinct(true)

            session.createQuery(criteriaQuery).resultList
        } finally {
            closeSession()
        }
    }

    // Метод для поиска книг по различным критериям
    fun findBooksByCriteria(
        title: String? = null,
        authorName: String? = null,
        genreName: String? = null,
        minYear: Int? = null,
        maxYear: Int? = null
    ): List<Book> {
        val session = getSession()
        return try {
            val criteriaBuilder: CriteriaBuilder = session.criteriaBuilder
            val criteriaQuery: CriteriaQuery<Book> = criteriaBuilder.createQuery(Book::class.java)
            val bookRoot: Root<Book> = criteriaQuery.from(Book::class.java)

            val predicates = mutableListOf<Predicate>()

            if (!title.isNullOrBlank()) {
                predicates.add(criteriaBuilder.like(bookRoot.get<String>("title"), "%$title%"))
            }

            if (!authorName.isNullOrBlank()) {
                val authorJoin = bookRoot.join<Book, Any>("author")
                predicates.add(criteriaBuilder.like(authorJoin.get<String>("name"), "%$authorName%"))
            }

            if (!genreName.isNullOrBlank()) {
                val genreJoin = bookRoot.join<Book, Any>("genre")
                predicates.add(criteriaBuilder.equal(genreJoin.get<String>("name"), genreName))
            }

            if (minYear != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(bookRoot.get<Int>("publicationYear"), minYear))
            }

            if (maxYear != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(bookRoot.get<Int>("publicationYear"), maxYear))
            }

            criteriaQuery.select(bookRoot)
            if (predicates.isNotEmpty()) {
                criteriaQuery.where(*predicates.toTypedArray())
            }
            criteriaQuery.orderBy(criteriaBuilder.asc(bookRoot.get<String>("title")))

            session.createQuery(criteriaQuery).resultList
        } finally {
            closeSession()
        }
    }
}