package com.junlog.comment.domain.model

interface CommentPort {
    fun deleteAllByArticleId(articleId: Long)

    fun persist(comment: Comment): Comment

    fun loadByIdAndUserId(id: Long, userId: Long): Comment?

    fun delete(comment: Comment)
}