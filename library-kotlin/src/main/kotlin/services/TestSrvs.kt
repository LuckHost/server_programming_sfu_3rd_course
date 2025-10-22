package edu.sfu.services

import edu.sfu.manager.DAO
import org.hibernate.query.NativeQuery
import org.hibernate.query.Query

class TestSrvs {

    companion object {

        // Метод 1: Получение имени автора по ID
        fun getAuthorName(id: Long): String? {
            val session = DAO.getSession()
            return try {
                val query: Query<String> = session.createQuery(
                    "SELECT a.name FROM Author a WHERE a.id = :id", String::class.java
                )
                query.setParameter("id", id)
                query.uniqueResult()
            } finally {
                DAO.close()
            }
        }

        // Метод 2: Получение списка email пользователей в диапазоне ID
        fun getUserEmailsInRange(startId: Long, endId: Long): List<String> {
            val session = DAO.getSession()
            return try {
                val query: Query<String> = session.createQuery(
                    "SELECT u.email FROM User u WHERE u.id BETWEEN :startId AND :endId",
                    String::class.java
                )
                query.setParameter("startId", startId)
                query.setParameter("endId", endId)
                query.list()
            } finally {
                DAO.close()
            }
        }

        // Метод 3: Получение названий жанров
        fun getGenreNames(): List<String> {
            val session = DAO.getSession()
            return try {
                val query: Query<String> = session.createQuery(
                    "SELECT g.name FROM Genre g",
                    String::class.java
                )
                query.list()
            } finally {
                DAO.close()
            }
        }

        // Метод 4: Создание нового жанра (с транзакцией) - ИСПРАВЛЕННАЯ ВЕРСИЯ
        fun createGenre(name: String, description: String?) {
            DAO.begin()
            val session = DAO.getSession()
            try {
                // Используем правильный тип для COUNT запроса
                val checkQuery: Query<Long> = session.createQuery(
                    "SELECT COUNT(g) FROM Genre g WHERE g.name = :name", Long::class.javaObjectType
                )
                checkQuery.setParameter("name", name)
                val count = checkQuery.uniqueResult()

                if (count > 0) {
                    println("Жанр '$name' уже существует, пропускаем создание")
                } else {
                    // Используем современный API вместо deprecated createSQLQuery
                    val insertQuery = session.createNativeQuery(
                        "INSERT INTO genres (name, description) VALUES (:name, :description)"
                    )
                    insertQuery.setParameter("name", name)
                    insertQuery.setParameter("description", description)
                    insertQuery.executeUpdate()
                    println("Жанр '$name' успешно создан!")
                }
                DAO.commit()
            } catch (e: Exception) {
                System.err.println("Ошибка при создании жанра: ${e.message}")
                throw e
            } finally {
                DAO.close()
            }
        }

        // Метод 5: Альтернативный способ создания жанра с ON CONFLICT
        fun createGenreSafe(name: String, description: String?) {
            DAO.begin()
            val session = DAO.getSession()
            try {
                val insertQuery = session.createNativeQuery(
                    "INSERT INTO genres (name, description) VALUES (:name, :description) " +
                            "ON CONFLICT (name) DO NOTHING"
                )
                insertQuery.setParameter("name", name)
                insertQuery.setParameter("description", description)
                val result = insertQuery.executeUpdate()

                if (result > 0) {
                    println("Жанр '$name' успешно создан!")
                } else {
                    println("Жанр '$name' уже существует, пропускаем создание")
                }
                DAO.commit()
            } catch (e: Exception) {
                System.err.println("Ошибка при создании жанра: ${e.message}")
                throw e
            } finally {
                DAO.close()
            }
        }

        // Метод 6: Создание нового пользователя (дополнительный метод)
        fun createUser(email: String, firstName: String, lastName: String) {
            DAO.begin()
            val session = DAO.getSession()
            try {
                val insertQuery = session.createNativeQuery(
                    "INSERT INTO users (email, first_name, last_name, registration_date) " +
                            "VALUES (:email, :firstName, :lastName, CURRENT_DATE)"
                )
                insertQuery.setParameter("email", email)
                insertQuery.setParameter("firstName", firstName)
                insertQuery.setParameter("lastName", lastName)
                val result = insertQuery.executeUpdate()

                if (result > 0) {
                    println("Пользователь '$email' успешно создан!")
                } else {
                    println("Не удалось создать пользователя '$email'")
                }
                DAO.commit()
            } catch (e: Exception) {
                System.err.println("Ошибка при создании пользователя: ${e.message}")
                throw e
            } finally {
                DAO.close()
            }
        }
    }
}