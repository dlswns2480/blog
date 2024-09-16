package com.junlog.article.infra

import com.junlog.article.domain.entity.ArticleEntity
import org.springframework.data.jpa.repository.JpaRepository

interface ArticleRepository : JpaRepository<ArticleEntity, Long> {
    fun findByIdAndUserIdAndDeleted(id: Long, userId: Long, deleted: Boolean): ArticleEntity?

    fun findByIdAndDeleted(id: Long, deleted: Boolean): ArticleEntity?
}