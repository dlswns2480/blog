package com.junlog.comment.domain.model

data class Comment(
    val id: Long = 0L,
    val userId: Long,
    val articleId: Long,
    val content: String
)
