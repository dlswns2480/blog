package com.junlog.article.infra

import com.junlog.article.domain.entity.ArticleEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface ArticleRepository : JpaRepository<ArticleEntity, Long> {
    fun findByIdAndUserIdAndDeleted(id: Long, userId: Long, deleted: Boolean): ArticleEntity?

    fun findByIdAndDeleted(id: Long, deleted: Boolean): ArticleEntity?

    @Modifying(clearAutomatically = true)
    @Query("""
        update ArticleEntity a set a.deleted = true 
        where a.userId = :userId
    """)
    fun deleteAllByUserId(@Param("userId") userId: Long)
}