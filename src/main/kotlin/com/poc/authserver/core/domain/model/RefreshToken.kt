package com.poc.authserver.core.domain.model

import com.poc.authserver.core.domain.valueobject.Token
import java.time.Instant
import java.util.UUID

data class RefreshToken(
    val token: Token,
    val expirationDate: Instant,
    val userIdentifier : UUID,
)
