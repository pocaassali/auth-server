package com.poc.authserver.infrastructure.persistence.entity

import com.poc.authserver.core.domain.model.RefreshToken
import com.poc.authserver.core.domain.valueobject.Token
import java.time.Instant
import java.util.*

data class RefreshTokenEntity(
    val token: String,
    val userId: String,
    val expiredAt: Instant,
) {
    fun toRefreshToken(): RefreshToken {
        return RefreshToken(
            token = Token(token),
            userIdentifier = UUID.fromString(userId),
            expirationDate = expiredAt
        )
    }

    companion object {
        fun from(refreshToken: RefreshToken): RefreshTokenEntity {
            return RefreshTokenEntity(
                token = refreshToken.token.value,
                userId = refreshToken.userIdentifier.toString(),
                expiredAt = refreshToken.expirationDate
            )
        }
    }
}
