package com.junlog.auth.exception

import com.junlog.common.exception.ErrorCode

enum class AuthErrorCode(
    override val message: String,
    override val code: String,
) : ErrorCode {
    TOKEN_EXPIRED("토큰이 만료되었습니다.", "A_000"),
    INVALID_TOKEN("유효하지 않은 토큰입니다.", "A_001"),
    UNSUPPORTED_TOKEN("지원하지 않는 형식의 토큰입니다.", "A_002"),
    WRONG_SIGNATURE("JWT 서명이 서버에 산정된 서명과 일치하지 않습니다.", "A_003"),
    TOKEN_REQUIRED("토큰이 비어있습니다.", "A_004"),
}