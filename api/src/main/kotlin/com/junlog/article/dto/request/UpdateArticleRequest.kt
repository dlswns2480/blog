package com.junlog.article.dto.request

import jakarta.validation.constraints.NotBlank

data class UpdateArticleRequest(
    @field:NotBlank(message = "필수 값입니다.")
    val title: String,
    @field:NotBlank(message = "필수 값입니다.")
    val content: String
)

internal fun UpdateArticleRequest.toDto() = ArticleCommand(
    title = this.title,
    content = this.content
)