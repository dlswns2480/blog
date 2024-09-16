package com.junlog.article.dto.request

import com.junlog.article.dto.request.ArticleCommand
import jakarta.validation.constraints.NotBlank

data class CreateArticleRequest(
    @field:NotBlank(message = "필수 값입니다.")
    val title: String,
    @field:NotBlank(message = "필수 값입니다.")
    val content: String
)

internal fun CreateArticleRequest.toDto() = ArticleCommand(
    title = this.title,
    content = this.content
)
