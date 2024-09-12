package com.junlog.user.exception

import com.junlog.common.exception.ErrorCode

enum class UserErrorCode(
    override val message: String,
    override val code: String
) : ErrorCode{
    NOT_FOUND_USER("존재하지 않는 유저입니다.", "U_001")
}