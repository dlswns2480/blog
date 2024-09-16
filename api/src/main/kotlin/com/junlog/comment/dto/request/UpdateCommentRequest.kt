package com.junlog.comment.dto.request

import jakarta.validation.constraints.NotBlank

data class UpdateCommentRequest(
    @field:NotBlank(message = "필수 값입니다.")
    val content: String
)

internal fun UpdateCommentRequest.toDto() = CommentCommand(
    content = this.content
)
