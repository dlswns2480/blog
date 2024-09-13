package com.junlog.user.dto.request

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class SignInRequest(
    @field:Email(message = "이메일 형식이 올바르지 않습니다.")
    @field:NotBlank(message = "필수 값입니다.")
    val email: String,
    @field:NotBlank(message = "필수 값입니다.")
    val password: String
)

internal fun SignInRequest.toDto() = SignInCommand(
    email = this.email,
    password = this.password
)