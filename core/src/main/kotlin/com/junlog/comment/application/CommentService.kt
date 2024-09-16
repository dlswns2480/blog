package com.junlog.comment.application

import com.junlog.article.application.validate
import com.junlog.article.domain.model.ArticlePort
import com.junlog.comment.domain.model.Comment
import com.junlog.comment.domain.model.CommentPort
import com.junlog.comment.dto.request.CommentCommand
import com.junlog.comment.dto.response.CommentInfo
import com.junlog.comment.dto.response.toCommentInfo
import com.junlog.comment.exception.CommentErrorCode
import com.junlog.common.exception.NotFoundCustomException
import com.junlog.user.domain.model.User
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class CommentService(
    private val commentPort: CommentPort,
    private val articlePort: ArticlePort
) {
    @Transactional
    fun create(user: User, articleId: Long, command: CommentCommand): CommentInfo {
        val article = articlePort.validate(articleId, user.id)
        val comment = Comment(
            userId = user.id,
            articleId = article.id,
            content = command.content
        )
        return commentPort.persist(comment).toCommentInfo(user.email)
    }

    @Transactional
    fun update(user: User, commentId: Long, command: CommentCommand): CommentInfo {
        val comment = commentPort.validate(commentId, user.id)
        comment.edit(command.content)
        return commentPort.persist(comment).toCommentInfo(user.email)
    }

    @Transactional
    fun delete(user: User, commentId: Long) {
        val comment = commentPort.validate(commentId, user.id)
        commentPort.delete(comment)
    }
}

fun CommentPort.validate(commentId: Long, userId: Long) = loadByIdAndUserId(commentId, userId)
    ?: throw NotFoundCustomException(CommentErrorCode.NOT_FOUND_COMMENT)