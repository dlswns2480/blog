package com.junlog.article.dto.response

import com.junlog.article.domain.model.Article

data class ArticleInfo(
    val articleId: Long,
    val email: String,
    val title: String,
    val content: String
)

internal fun Article.toArticleInfo(email: String) = ArticleInfo(
    articleId = this.id,
    email = email,
    title = this.title,
    content = this.content
)