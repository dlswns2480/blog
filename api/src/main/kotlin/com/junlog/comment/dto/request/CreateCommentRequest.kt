package com.junlog.comment.dto.request

import jakarta.validation.constraints.NotBlank

data class CreateCommentRequest(
    @field:NotBlank(message = "필수 값입니다.")
    val articleId: Long,
    @field:NotBlank(message = "필수 값입니다.")
    val content: String
)

internal fun CreateCommentRequest.toDto() = CommentCommand(
    content = this.content
)
