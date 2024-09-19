package com.junlog.article.application

import com.junlog.article.domain.model.Article
import com.junlog.article.domain.model.ArticlePort
import com.junlog.article.dto.request.ArticleCommand
import com.junlog.article.dto.response.ArticleInfo
import com.junlog.article.dto.response.toArticleInfo
import com.junlog.article.exception.ArticleErrorCode
import com.junlog.comment.domain.model.CommentPort
import com.junlog.common.exception.NotFoundCustomException
import com.junlog.user.domain.model.User
import com.junlog.user.domain.model.UserPort
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class ArticleService(
    private val userPort: UserPort,
    private val articlePort: ArticlePort,
    private val commentPort: CommentPort
) {
    private val logger = KotlinLogging.logger { }

    @Transactional
    fun create(user: User, command: ArticleCommand): ArticleInfo {
        val article = Article(
            userId = user.id,
            title = command.title,
            content = command.content
        )
        val savedArticle = articlePort.persist(article)
        return savedArticle.toArticleInfo(user.email)
    }

    @Transactional
    fun update(user: User, articleId: Long, command: ArticleCommand): ArticleInfo {
        val article = articlePort.validate(user.id, articleId)
        article.edit(command.title, command.content)
        return articlePort.persist(article).toArticleInfo(user.email)
    }

    @Transactional
    fun delete(user: User, articleId: Long) {
        val article = articlePort.validate(user.id, articleId)
        commentPort.deleteAllByArticleId(article.id)
        articlePort.delete(article)
    }
}

fun ArticlePort.validate(userId: Long, articleId: Long) = loadByIdAndUserId(userId, articleId)
    ?: throw NotFoundCustomException(ArticleErrorCode.NOT_FOUND_ARTICLE)