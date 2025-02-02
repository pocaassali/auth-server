package com.poc.authserver.core.application.dto.command

import com.poc.authserver.core.domain.model.RefreshToken
import com.poc.authserver.core.domain.valueobject.Token
import java.time.Instant
import java.util.*

data class SaveRefreshTokenCommand(
    val refreshToken: String,
    val expiresIn: Instant,
    val userIdentifier : String,
) {
    fun toRefreshToken(): RefreshToken {
        return RefreshToken(
            token = Token(refreshToken),
            expirationDate = expiresIn,
            userIdentifier = UUID.fromString(userIdentifier),
        )
    }
}
