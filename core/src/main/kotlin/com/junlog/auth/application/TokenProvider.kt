package com.junlog.auth.application

import com.junlog.auth.domain.model.Token
import com.junlog.auth.exception.AuthErrorCode
import com.junlog.auth.property.JwtProperty
import com.junlog.common.exception.ClientValidationException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Component
import java.util.*
import io.jsonwebtoken.*

@Component
class JwtTokenProvider(
    private val jwtProperty: JwtProperty,
) {
    companion object {
        private const val TO_DAY = 86400000
    }
    fun createToken(userId: Long): Token {
        val accessToken = generateToken(userId, jwtProperty.accessExpiryTime * TO_DAY)
        val refreshToken = generateToken(userId, jwtProperty.refreshExpiryTime * TO_DAY)

        return Token(accessToken, refreshToken)
    }

    fun getUserId(token: String): Long {
        val claims = getClaims(token)
        val userId = claims.payload.subject
        return userId.toLong()
    }

    private fun generateToken(
        id: Long,
        expireTime: Long,
    ): String {
        val now = Date()
        val expireDate = Date(now.time + expireTime)
        val keyBytes = Decoders.BASE64.decode(jwtProperty.clientSecret)

        val claims = Jwts.claims().subject(id.toString()).build()
        return Jwts.builder()
            .claims(claims)
            .issuedAt(now)
            .expiration(expireDate)
            .signWith(Keys.hmacShaKeyFor(keyBytes))
            .compact()
    }

    private fun getClaims(token: String): Jws<Claims> {
        val keyBytes = Decoders.BASE64.decode(jwtProperty.clientSecret)

        try {
            return Jwts.parser()
                .setSigningKey(Keys.hmacShaKeyFor(keyBytes))
                .build()
                .parseClaimsJws(token)
        } catch (e: ExpiredJwtException) {
            throw ClientValidationException(AuthErrorCode.TOKEN_EXPIRED)
        } catch (e: MalformedJwtException) {
            throw ClientValidationException(AuthErrorCode.INVALID_TOKEN)
        } catch (e: UnsupportedJwtException) {
            throw ClientValidationException(AuthErrorCode.UNSUPPORTED_TOKEN)
        } catch (e: SignatureException) {
            throw ClientValidationException(AuthErrorCode.WRONG_SIGNATURE)
        }
    }
}
