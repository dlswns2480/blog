package com.junlog.auth.infra

import com.junlog.auth.domain.entity.RefreshTokenEntity
import com.junlog.auth.domain.entity.toDomain
import com.junlog.auth.domain.model.RefreshToken
import com.junlog.auth.domain.model.RefreshTokenPort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class RefreshTokenAdapter(
    private val refreshTokenRepository: RefreshTokenRepository
) : RefreshTokenPort {
    override fun persist(refreshToken: RefreshToken): RefreshToken {
        return refreshTokenRepository.save(RefreshTokenEntity.of(refreshToken))
            .toDomain()
    }

    override fun loadByToken(token: String): RefreshToken? {
        return refreshTokenRepository.findByToken(token)
            ?.run { toDomain() }
    }

    override fun deleteByUserId(userId: Long) {
        refreshTokenRepository.deleteByUserId(userId)
    }
}