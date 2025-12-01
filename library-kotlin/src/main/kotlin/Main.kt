package edu.sfu

import edu.sfu.manager.DAO
import edu.sfu.services.TestSrvs

fun main() {
    println("=== SFU Library Management System (Kotlin) ===")

    try {
        // Демонстрация ПЗ №4 (базовые методы)
        demonstrateLab4()

        // Демонстрация ПЗ №5 (Criteria API и DAO)
        Lab5Demo.demonstrate()

    } catch (e: Exception) {
        System.err.println("Ошибка: ${e.message}")
        e.printStackTrace()
    } finally {
        // Корректно закрываем Hibernate только в самом конце
        DAO.getSessionFactory().close()
        println("\nSessionFactory закрыт корректно")
    }

    println("\n=== Программа завершена ===")
}

private fun demonstrateLab4() {
    println("\n--- ДЕМОНСТРАЦИЯ ПЗ №4 ---")

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

        // Тест 3: Создание нового жанра
        println("\n3. Тест создания нового жанра:")
        val uniqueGenreName = "ТестовыйЖанр_${System.currentTimeMillis()}"
        TestSrvs.createGenre(uniqueGenreName, "Описание тестового жанра")

    } catch (e: Exception) {
        System.err.println("Ошибка в демонстрации ПЗ №4: ${e.message}")
    }
}