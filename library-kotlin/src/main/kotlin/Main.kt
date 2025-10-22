package edu.sfu

import edu.sfu.manager.DAO
import edu.sfu.services.TestSrvs

fun main() {
    println("=== SFU Library Management System (Kotlin) ===")

    try {
        // Тест 1: Получение имени автора по ID
        println("\n1. Тест получения имени автора:")
        val authorName = TestSrvs.getAuthorName(1L)
        println("Автор с ID=1: $authorName")

        // Тест 2: Получение email пользователей в диапазоне
        println("\n2. Тест получения email пользователей:")
        val emails = TestSrvs.getUserEmailsInRange(1L, 5L)
        println("Email пользователей с ID 1-5:")
        emails.forEach { email ->
            println("  - $email")
        }

        // Тест 3: Создание нового жанра (уникальное имя)
        println("\n3. Тест создания нового жанра:")
        val uniqueGenreName = "Биотехнологии_${System.currentTimeMillis()}"
        TestSrvs.createGenre(uniqueGenreName, "Книги о биотехнологиях и генной инженерии")

        // Тест 4: Попытка создать существующий жанр
        println("\n4. Тест создания существующего жанра:")
        TestSrvs.createGenre("Фантастика", "Попытка создать дубликат")

        // Тест 5: Создание жанра безопасным методом
        println("\n5. Тест безопасного создания жанра:")
        TestSrvs.createGenreSafe("Космическая опера", "Эпические космические саги")

        // Тест 6: Создание нового пользователя
        println("\n6. Тест создания нового пользователя:")
        val uniqueEmail = "testuser_${System.currentTimeMillis()}@example.com"
        TestSrvs.createUser(uniqueEmail, "Тест", "Пользователь")

        // Дополнительный тест: получение нескольких авторов
        println("\n7. Дополнительный тест авторов:")
        for (i in 1L..3L) {
            val name = TestSrvs.getAuthorName(i)
            println("Автор с ID=$i: $name")
        }

        // Тест 8: Получение всех жанров
        println("\n8. Тест получения всех жанров:")
        val genres = TestSrvs.getGenreNames()
        println("Все жанры в системе (первые 5):")
        genres.take(5).forEach { genre ->
            println("  - $genre")
        }

    } catch (e: Exception) {
        System.err.println("Ошибка: ${e.message}")
        e.printStackTrace()
    } finally {
        // Корректно закрываем Hibernate
        DAO.getSessionFactory().close()
    }

    println("\n=== Программа завершена ===")
}