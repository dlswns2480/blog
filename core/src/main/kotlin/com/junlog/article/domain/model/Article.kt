package com.junlog.article.domain.model

data class Article(
    val id: Long = 0L,
    val userId: Long,
    val title: String,
    val content: String
)