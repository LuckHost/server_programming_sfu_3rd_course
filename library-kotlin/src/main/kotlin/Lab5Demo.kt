package edu.sfu

import edu.sfu.dao.*
import edu.sfu.entity.*
import edu.sfu.manager.DAO
import java.time.LocalDate

class Lab5Demo {

    companion object {

        fun demonstrate() {
            println("\n=== ДЕМОНСТРАЦИЯ ПЗ №5 ===")

            val authorDAO = AuthorDAO()
            val bookDAO = BookDAO()
            val genreDAO = GenreDAO()
            val userDAO = UserDAO()

            try {
                // 1. Демонстрация Criteria API - получение всех элементов
                println("\n1. Criteria API - все авторы (первые 5):")
                val allAuthors = authorDAO.findAll().take(5)
                allAuthors.forEach { println("  - $it") }

                // 2. Демонстрация динамических фильтров
                println("\n2. Динамические фильтры - авторы из России:")
                val russianAuthors = authorDAO.findWithFilters(country = "Россия")
                russianAuthors.forEach { println("  - $it") }

                // 3. Criteria API запрос из ПЗ №3
                println("\n3. Criteria API запрос из ПЗ №3 - просроченные книги:")
                val overdueBooks = bookDAO.findOverdueBooksAlternative()
                if (overdueBooks.isNotEmpty()) {
                    overdueBooks.take(3).forEach { println("  - ${it.title} (ID: ${it.id})") }
                } else {
                    println("  Нет просроченных книг по заданным критериям")
                }

                // 4. Демонстрация CRUD операций
                println("\n4. CRUD операции:")

                // Создание нового автора
                val newAuthor = Author(
                    name = "Тестовый Автор ${System.currentTimeMillis()}",
                    country = "Тестовая Страна"
                )
                val savedAuthor = authorDAO.save(newAuthor)
                println("  Создан автор: ${savedAuthor.name} (ID: ${savedAuthor.id})")

                // Поиск по ID
                val foundAuthor = authorDAO.findById(savedAuthor.id)
                println("  Найден автор по ID ${savedAuthor.id}: ${foundAuthor?.name}")

                // 5. Демонстрация связи один-ко-многим (пункт 7 задания)
                println("\n5. Демонстрация связи один-ко-многим:")
                demonstrateOneToManyRelationship()

                // 6. Переписанные методы из ПЗ №4 на Criteria API
                println("\n6. Методы из ПЗ №4 на Criteria API:")

                val authorName = authorDAO.getAuthorNameById(1L)
                println("  Имя автора с ID=1: $authorName")

                val userEmails = userDAO.getUserEmailsInRange(1L, 3L)
                println("  Email пользователей 1-3: $userEmails")

                val genreNames = genreDAO.getGenreNames().take(5)
                println("  Первые 5 жанров: $genreNames")

                // 7. Поиск книг с различными критериями
                println("\n7. Поиск книг с критериями:")
                val fantasyBooks = bookDAO.findBooksByCriteria(genreName = "Фантастика")
                if (fantasyBooks.isNotEmpty()) {
                    fantasyBooks.take(3).forEach { println("  - ${it.title}") }
                } else {
                    println("  Не найдено книг жанра 'Фантастика'")
                }

                // 8. Дополнительно: показываем количество книг у авторов
                println("\n8. Количество книг у авторов (первые 3):")
                allAuthors.take(3).forEach { author ->
                    // Используем безопасный метод или отдельный запрос
                    val booksCount = bookDAO.findBooksByCriteria(authorName = author.name).size
                    println("  ${author.name}: $booksCount книг")
                }

            } catch (e: Exception) {
                System.err.println("Ошибка при демонстрации: ${e.message}")
                e.printStackTrace()
            }
        }

        // Упрощенная демонстрация связи один-ко-многим
        private fun demonstrateOneToManyRelationship() {
            val authorDAO = AuthorDAO()
            val bookDAO = BookDAO()
            val genreDAO = GenreDAO()


            try {
                // Находим автора с наибольшим количеством книг
                println("  Поиск авторов и их книг...")

                // Найдем всех авторов
                val authors = authorDAO.findAll().take(5)

                authors.forEach { author ->
                    val books = bookDAO.findBooksByCriteria(authorName = author.name)
                    if (books.isNotEmpty()) {
                        println("  Автор: ${author.name}")
                        println("  Количество книг: ${books.size}")
                        println("  Первые 2 книги:")
                        books.take(2).forEach { book ->
                            println("    - ${book.title} (${book.publicationYear})")
                        }
                        println()
                    }
                }

                if (authors.all { bookDAO.findBooksByCriteria(authorName = it.name).isEmpty() }) {
                    println("  Не найдено авторов с книгами. Создаем тестовые данные...")

                    // Создаем тестового автора и книгу
                    val testAuthor = authorDAO.save(Author(
                        name = "Демо Автор ${System.currentTimeMillis()}",
                        country = "Демо Страна"
                    ))

                    val genre = genreDAO.findAll().firstOrNull()
                    if (genre != null) {
                        val testBook = Book(
                            title = "Демо Книга для ${testAuthor.name}",
                            publicationYear = 2024,
                            author = testAuthor,
                            genre = genre
                        )

                        bookDAO.save(testBook)
                        println("  Создана связь: Автор '${testAuthor.name}' → Книга '${testBook.title}'")
                    }
                }

            } catch (e: Exception) {
                System.err.println("Ошибка при демонстрации связи: ${e.message}")
            }
        }
    }
}