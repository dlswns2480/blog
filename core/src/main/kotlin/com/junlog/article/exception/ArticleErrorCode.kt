package com.junlog.article.exception

import com.junlog.common.exception.ErrorCode

enum class ArticleErrorCode(
    override val message: String,
    override val code: String
) : ErrorCode {
    NOT_FOUND_ARTICLE("존재하지 않는 게시물입니다.", "AT_001")
}