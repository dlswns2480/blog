package com.junlog.article

import com.junlog.article.domain.model.Article
import com.junlog.article.dto.request.ArticleCommand

class ArticleFixture {
    companion object {
        fun get(userId: Long) = Article(
            id = 1L,
            userId = userId,
            title = "title1",
            content = "content1",
        )

        fun getCommand() = ArticleCommand(
            title = "title1",
            content = "content"
        )

        fun getUpdateCommand() = ArticleCommand(
            title = "updatedTitle",
            content = "updatedContent"
        )
    }
}