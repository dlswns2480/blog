package com.junlog.user.dto.request

data class UserCommand(
    val email: String,
    val password: String,
    val username: String
)
