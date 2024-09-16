package com.junlog.comment.domain.model

data class Comment(
    val id: Long = 0L,
    val userId: Long,
    val articleId: Long,
    var content: String
) {
    fun edit(content: String) {
        this.content = content
    }
}
