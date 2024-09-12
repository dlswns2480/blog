package com.junlog.user.domain.model

data class User(
    val id: Long = 0L,
    val email: String,
    val password: String,
    val username: String,
    val role: Role
)