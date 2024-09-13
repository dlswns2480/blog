package com.junlog.auth.domain.entity

import com.junlog.auth.domain.model.RefreshToken
import jakarta.persistence.*

@Table(name = "REFRESH_TOKEN")
@Entity
class RefreshTokenEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long = 0L,

    @Column(name = "user_id")
    val userId: Long,

    @Column(name = "token")
    val token: String,
) {
    companion object {
        fun of(refreshToken: RefreshToken) = RefreshTokenEntity(
            userId = refreshToken.userId,
            token = refreshToken.token
        )
    }
}

internal fun RefreshTokenEntity.toDomain() = RefreshToken(
    id = this.id,
    userId = this.userId,
    token = this.token
)