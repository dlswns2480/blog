package com.junlog.article.domain.model

interface ArticlePort {
    fun persist(article: Article): Article

    fun loadByIdAndUserId(id: Long, userId: Long): Article?

    fun delete(article: Article)

    fun loadById(articleId: Long): Article?
}