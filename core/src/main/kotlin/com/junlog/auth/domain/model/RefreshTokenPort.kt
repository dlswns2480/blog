package com.junlog.auth.domain.model

interface RefreshTokenPort {
    fun persist(refreshToken: RefreshToken): RefreshToken

    fun loadByToken(token: String): RefreshToken?

    fun deleteByUserId(userId: Long)
}