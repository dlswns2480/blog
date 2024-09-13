package com.junlog.user.dto.request

data class SignInCommand(
    val email: String,
    val password: String
)