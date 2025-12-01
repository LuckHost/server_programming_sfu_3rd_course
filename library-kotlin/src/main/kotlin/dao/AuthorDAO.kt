package edu.sfu.dao

import edu.sfu.entity.Author
import org.hibernate.Session
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root

class AuthorDAO : BaseDAO<Author>() {
    override fun getEntityClass(): Class<Author> = Author::class.java
    
    // Метод с динамическими фильтрами (пункт 4 задания)
    fun findWithFilters(name: String? = null, country: String? = null, minBooks: Int? = null): List<Author> {
        val session = getSession()
        return try {
            val criteriaBuilder: CriteriaBuilder = session.criteriaBuilder
            val criteriaQuery: CriteriaQuery<Author> = criteriaBuilder.createQuery(Author::class.java)
            val root: Root<Author> = criteriaQuery.from(Author::class.java)
            
            val predicates = mutableListOf<Predicate>()
            
            // Динамически добавляем условия в зависимости от параметров
            if (!name.isNullOrBlank()) {
                predicates.add(criteriaBuilder.like(root.get<String>("name"), "%$name%"))
            }
            
            if (!country.isNullOrBlank()) {
                predicates.add(criteriaBuilder.equal(root.get<String>("country"), country))
            }
            
            if (minBooks != null) {
                // Подзапрос для подсчета книг автора
                val subquery = criteriaQuery.subquery(Long::class.javaObjectType)
                val bookRoot = subquery.from(Author::class.java)
                subquery.select(criteriaBuilder.count(bookRoot.get<Any>("id")))
                subquery.where(criteriaBuilder.equal(bookRoot.get<Any>("id"), root.get<Any>("id")))
                
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(subquery, minBooks.toLong()))
            }
            
            criteriaQuery.select(root)
            if (predicates.isNotEmpty()) {
                criteriaQuery.where(*predicates.toTypedArray())
            }
            criteriaQuery.orderBy(criteriaBuilder.asc(root.get<String>("name")))
            
            session.createQuery(criteriaQuery).resultList
        } finally {
            closeSession()
        }
    }
    
    // Criteria API версия метода из ПЗ №4
    fun getAuthorNameById(id: Long): String? {
        val session = getSession()
        return try {
            val criteriaBuilder: CriteriaBuilder = session.criteriaBuilder
            val criteriaQuery: CriteriaQuery<String> = criteriaBuilder.createQuery(String::class.java)
            val root: Root<Author> = criteriaQuery.from(Author::class.java)
            
            criteriaQuery.select(root.get("name"))
            criteriaQuery.where(criteriaBuilder.equal(root.get<Long>("id"), id))
            
            session.createQuery(criteriaQuery).uniqueResult()
        } finally {
            closeSession()
        }
    }
}