package com.junlog.article.infra

import com.junlog.article.domain.entity.ArticleEntity
import com.junlog.article.domain.entity.toDomain
import com.junlog.article.domain.model.Article
import com.junlog.article.domain.model.ArticlePort
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class ArticleAdapter(
    private val articleRepository: ArticleRepository
) : ArticlePort{
    override fun persist(article: Article): Article {
        val articleEntity = ArticleEntity.of(article)
        return articleRepository.save(articleEntity)
            .toDomain()
    }

    override fun loadByIdAndUserId(id: Long, userId: Long): Article? {
        return articleRepository.findByIdAndUserIdAndDeleted(id, userId, false)
            ?.run { toDomain() }
    }

    override fun delete(article: Article) {
        articleRepository.findByIdOrNull(article.id)
            ?.delete()
    }

    override fun loadById(articleId: Long): Article? {
        return articleRepository.findByIdAndDeleted(articleId, false)
            ?.run { toDomain() }
    }
}