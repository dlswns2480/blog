package com.junlog.comment.domain.model

interface CommentPort {
    fun deleteAllByArticleId(articleId: Long)
}