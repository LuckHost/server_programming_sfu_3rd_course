package edu.sfu.dao

import edu.sfu.manager.DAO
import org.hibernate.Session
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Root

abstract class BaseDAO<T> {

    protected abstract fun getEntityClass(): Class<T>

    // CRUD операции
    fun findById(id: Long): T? {
        val session = getSession()
        return try {
            session.find(getEntityClass(), id)
        } finally {
            closeSession()
        }
    }

    fun save(entity: T): T {
        beginTransaction()
        val session = getSession()
        return try {
            session.saveOrUpdate(entity)
            commitTransaction()
            entity
        } catch (e: Exception) {
            rollbackTransaction()
            throw e
        } finally {
            closeSession()
        }
    }

    fun update(entity: T): T {
        beginTransaction()
        val session = getSession()
        return try {
            session.update(entity)
            commitTransaction()
            entity
        } catch (e: Exception) {
            rollbackTransaction()
            throw e
        } finally {
            closeSession()
        }
    }

    fun delete(entity: T) {
        beginTransaction()
        val session = getSession()
        try {
            session.delete(entity)
            commitTransaction()
        } catch (e: Exception) {
            rollbackTransaction()
            throw e
        } finally {
            closeSession()
        }
    }

    fun deleteById(id: Long) {
        val entity = findById(id)
        if (entity != null) {
            delete(entity)
        }
    }

    // Criteria API методы
    fun findAll(): List<T> {
        val session = getSession()
        return try {
            val criteriaBuilder: CriteriaBuilder = session.criteriaBuilder
            val criteriaQuery: CriteriaQuery<T> = criteriaBuilder.createQuery(getEntityClass())
            val root: Root<T> = criteriaQuery.from(getEntityClass())
            criteriaQuery.select(root)

            val query = session.createQuery(criteriaQuery)
            query.resultList
        } finally {
            closeSession()
        }
    }

    // Вспомогательные методы
    protected fun getSession(): Session = DAO.getSession()
    protected fun beginTransaction() = DAO.begin()
    protected fun commitTransaction() = DAO.commit()
    protected fun rollbackTransaction() = DAO.rollback()
    protected fun closeSession() = DAO.close()
}