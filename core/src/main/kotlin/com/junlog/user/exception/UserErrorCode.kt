package com.junlog.user.exception

import com.junlog.common.exception.ErrorCode

enum class UserErrorCode(
    override val message: String,
    override val code: String
) : ErrorCode{
    NOT_FOUND_USER("존재하지 않는 유저입니다.", "U_001"),
    WRONG_PASSWORD("비밀번호가 올바르지 않습니다", "U_002"),
    ALREADY_EXISTS_NAME("이미 존재하는 이름(닉네임)입니다.", "U_003")
}