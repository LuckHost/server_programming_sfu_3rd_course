package edu.sfu.manager

import org.hibernate.Session
import org.hibernate.SessionFactory
import org.hibernate.cfg.Configuration

object DAO {
    private val sessionThreadLocal = ThreadLocal<Session>()
    private var sessionFactory: SessionFactory? = null

    fun getSessionFactory(): SessionFactory {
        if (sessionFactory == null || sessionFactory!!.isClosed) {
            synchronized(this) {
                if (sessionFactory == null || sessionFactory!!.isClosed) {
                    try {
                        sessionFactory = Configuration().configure("hibernate.cfg.xml").buildSessionFactory()
                    } catch (ex: Throwable) {
                        System.err.println("Initial SessionFactory creation failed: $ex")
                        throw ExceptionInInitializerError(ex)
                    }
                }
            }
        }
        return sessionFactory!!
    }

    fun getSession(): Session {
        var currentSession = sessionThreadLocal.get()
        if (currentSession == null || !currentSession.isOpen) {
            currentSession = getSessionFactory().openSession()
            sessionThreadLocal.set(currentSession)
        }
        return currentSession
    }

    fun begin() {
        val transaction = getSession().transaction
        if (!transaction.isActive) {
            getSession().beginTransaction()
        }
    }

    fun commit() {
        val currentSession = sessionThreadLocal.get()
        if (currentSession != null && currentSession.isOpen) {
            val transaction = currentSession.transaction
            if (transaction.isActive) {
                try {
                    transaction.commit()
                } catch (e: Exception) {
                    System.err.println("Ошибка при коммите транзакции: ${e.message}")
                    throw e
                }
            }
        }
    }

    fun rollback() {
        val currentSession = sessionThreadLocal.get()
        if (currentSession != null && currentSession.isOpen) {
            val transaction = currentSession.transaction
            if (transaction.isActive) {
                try {
                    transaction.rollback()
                } catch (e: Exception) {
                    System.err.println("Ошибка при откате транзакции: ${e.message}")
                }
            }
        }
    }

    fun close() {
        val currentSession = sessionThreadLocal.get()
        if (currentSession != null && currentSession.isOpen) {
            try {
                if (currentSession.transaction.isActive) {
                    currentSession.transaction.rollback()
                }
                currentSession.close()
            } catch (e: Exception) {
                System.err.println("Ошибка при закрытии сессии: ${e.message}")
            }
        }
        sessionThreadLocal.remove()
    }

    fun shutdown() {
        try {
            close()
            sessionFactory?.close()
            sessionFactory = null
        } catch (e: Exception) {
            System.err.println("Ошибка при остановке DAO: ${e.message}")
        }
    }
}