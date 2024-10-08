package com.junlog.auth.infra

import com.junlog.auth.domain.entity.RefreshTokenEntity
import org.springframework.data.jpa.repository.JpaRepository

interface RefreshTokenRepository : JpaRepository<RefreshTokenEntity, Long> {
    fun findByToken(token: String): RefreshTokenEntity?

    fun deleteByUserId(userId: Long)
}