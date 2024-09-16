package com.junlog.comment.dto.response

import com.junlog.comment.domain.model.Comment

data class CommentInfo(
    val articleId: Long,
    val email: String,
    val content: String
)

fun Comment.toCommentInfo(email: String) = CommentInfo(
    articleId = this.articleId,
    email = email,
    content = this.content
)