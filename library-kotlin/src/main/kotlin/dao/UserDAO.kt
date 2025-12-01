package edu.sfu.dao

import edu.sfu.entity.User
import java.time.LocalDate
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Root

class UserDAO : BaseDAO<User>() {
    override fun getEntityClass(): Class<User> = User::class.java
    
    // Criteria API версия метода из ПЗ №4
    fun getUserEmailsInRange(startId: Long, endId: Long): List<String> {
        val session = getSession()
        return try {
            val criteriaBuilder: CriteriaBuilder = session.criteriaBuilder
            val criteriaQuery: CriteriaQuery<String> = criteriaBuilder.createQuery(String::class.java)
            val root: Root<User> = criteriaQuery.from(User::class.java)
            
            criteriaQuery.select(root.get("email"))
            criteriaQuery.where(
                criteriaBuilder.between(root.get<Long>("id"), startId, endId)
            )
            criteriaQuery.orderBy(criteriaBuilder.asc(root.get<String>("email")))
            
            session.createQuery(criteriaQuery).resultList
        } finally {
            closeSession()
        }
    }
    
    // Метод для поиска пользователей по дате регистрации
    fun findUsersRegisteredAfter(date: LocalDate): List<User> {
        val session = getSession()
        return try {
            val criteriaBuilder: CriteriaBuilder = session.criteriaBuilder
            val criteriaQuery: CriteriaQuery<User> = criteriaBuilder.createQuery(User::class.java)
            val root: Root<User> = criteriaQuery.from(User::class.java)
            
            criteriaQuery.select(root)
            criteriaQuery.where(
                criteriaBuilder.greaterThanOrEqualTo(root.get("registrationDate"), date)
            )
            criteriaQuery.orderBy(criteriaBuilder.desc(root.get<LocalDate>("registrationDate")))
            
            session.createQuery(criteriaQuery).resultList
        } finally {
            closeSession()
        }
    }
}