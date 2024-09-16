package com.junlog.comment.exception

import com.junlog.common.exception.ErrorCode

enum class CommentErrorCode(
    override val message: String,
    override val code: String
) : ErrorCode {
    NOT_FOUND_COMMENT("댓글을 찾을 수 없습니다.", "C_001")
}