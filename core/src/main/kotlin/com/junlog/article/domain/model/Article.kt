package com.junlog.article.domain.model

data class Article(
    val id: Long = 0L,
    val userId: Long,
    var title: String,
    var content: String
) {
    fun edit(title: String, content: String) {
        this.title = title
        this.content = content
    }
}