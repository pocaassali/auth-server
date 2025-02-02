package com.poc.authserver.core.application.ports.output

import com.poc.authserver.core.domain.model.RefreshToken
import com.poc.authserver.core.domain.valueobject.Token
import java.util.UUID

interface Tokens {
    fun findByToken(token: Token): RefreshToken?
    fun delete(token: Token)
    fun delete(userId: UUID)
    fun save(refreshToken: RefreshToken) : RefreshToken?
}