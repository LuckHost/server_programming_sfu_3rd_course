package edu.sfu.manager

import org.hibernate.Session
import org.hibernate.SessionFactory
import org.hibernate.cfg.Configuration

object DAO {
    private val session: ThreadLocal<Session> = ThreadLocal()
    private val sessionFactory: SessionFactory

    init {
        try {
            sessionFactory = Configuration().configure("hibernate.cfg.xml").buildSessionFactory()
        } catch (ex: Throwable) {
            System.err.println("Initial SessionFactory creation failed: $ex")
            throw ExceptionInInitializerError(ex)
        }
    }

    fun getSession(): Session {
        var currentSession = session.get()
        if (currentSession == null || !currentSession.isOpen) {
            currentSession = sessionFactory.openSession()
            session.set(currentSession)
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
        val currentSession = session.get()
        if (currentSession != null && currentSession.isOpen) {
            val transaction = currentSession.transaction
            if (transaction.isActive) {
                transaction.commit()
            }
        }
    }

    fun close() {
        val currentSession = session.get()
        if (currentSession != null && currentSession.isOpen) {
            currentSession.close()
        }
        session.remove()
    }

    fun getSessionFactory(): SessionFactory = sessionFactory
}
