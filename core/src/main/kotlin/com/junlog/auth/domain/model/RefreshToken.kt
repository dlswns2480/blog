package com.junlog.auth.domain.model

data class RefreshToken(
    val id: Long = 0L,
    val userId: Long,
    val token: String
)
