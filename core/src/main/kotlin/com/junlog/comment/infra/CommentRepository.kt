package com.junlog.comment.infra

import com.junlog.comment.domain.entity.CommentEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface CommentRepository : JpaRepository<CommentEntity, Long> {
    @Modifying(clearAutomatically = true)
    @Query("""
        update CommentEntity c set c.deleted = true where c.articleId = :articleId
    """)
    fun deleteAllByArticleId(@Param("articleId") articleId: Long)
}