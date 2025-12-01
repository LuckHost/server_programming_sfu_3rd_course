package edu.sfu.dao

import edu.sfu.entity.Genre

class GenreDAO : BaseDAO<Genre>() {
    override fun getEntityClass(): Class<Genre> = Genre::class.java

    // Criteria API версия метода из ПЗ №4
    fun getGenreNames(): List<String> {
        val session = getSession()
        return try {
            val criteriaBuilder = session.criteriaBuilder
            val criteriaQuery = criteriaBuilder.createQuery(String::class.java)
            val root = criteriaQuery.from(Genre::class.java)

            criteriaQuery.select(root.get("name"))
            criteriaQuery.orderBy(criteriaBuilder.asc(root.get<String>("name")))

            session.createQuery(criteriaQuery).resultList
        } finally {
            closeSession()
        }
    }
}