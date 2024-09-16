package com.junlog.article.application

import com.junlog.article.domain.model.Article
import com.junlog.article.dto.response.ArticleInfo
import com.junlog.article.domain.model.ArticlePort
import com.junlog.article.dto.response.toArticleInfo
import com.junlog.article.dto.request.ArticleCommand
import com.junlog.article.exception.ArticleErrorCode
import com.junlog.comment.domain.model.CommentPort
import com.junlog.common.exception.NotFoundCustomException
import com.junlog.user.domain.model.User
import com.junlog.user.domain.model.UserPort
import com.junlog.user.exception.UserErrorCode
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.support.TransactionSynchronizationManager

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
        val findUser = validateUser(user.id)
        val article = Article(
            userId = findUser.id,
            title = command.title,
            content = command.content
        )
        val savedArticle = articlePort.persist(article)
        return savedArticle.toArticleInfo(findUser.email)
    }

    @Transactional
    fun update(user: User, articleId: Long, command: ArticleCommand): ArticleInfo {
        val article = validateArticle(user.id, articleId)
        article.edit(command.title, command.content)
        return articlePort.persist(article).toArticleInfo(user.email)
    }

    @Transactional
    fun delete(user: User, articleId: Long) {
        val article = validateArticle(user.id, articleId)
        commentPort.deleteAllByArticleId(article.id)
        articlePort.delete(article)
    }

    private fun validateArticle(userId: Long, articleId: Long): Article {
        return articlePort.loadByIdAndUserId(articleId, userId)
            ?: throw NotFoundCustomException(ArticleErrorCode.NOT_FOUND_ARTICLE)
    }

    private fun validateUser(userId: Long): User {
        return userPort.loadById(userId)
            ?: throw NotFoundCustomException(UserErrorCode.NOT_FOUND_USER)
    }
}