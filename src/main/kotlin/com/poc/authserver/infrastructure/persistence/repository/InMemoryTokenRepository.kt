package com.poc.authserver.infrastructure.persistence.repository

import com.poc.authserver.core.application.ports.output.Tokens
import com.poc.authserver.core.domain.model.RefreshToken
import com.poc.authserver.core.domain.valueobject.Token
import com.poc.authserver.infrastructure.persistence.entity.RefreshTokenEntity
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class InMemoryTokenRepository : Tokens {

    private val tokens = mutableMapOf<Long,RefreshTokenEntity>()

    override fun findByToken(token: Token): RefreshToken? {
        return tokens.values.find { it.token == token.value }?.toRefreshToken()
    }

    override fun delete(token: Token) {
        val tokenToDelete = tokens.entries.find { it.value.token == token.value }
        tokenToDelete?.let { tokens.remove(it.key) }
    }

    override fun delete(userId: UUID) {
        val tokenToDelete = tokens.entries.find { it.value.userId == userId.toString() }
        tokenToDelete?.let { tokens.remove(it.key) }
    }

    override fun save(refreshToken: RefreshToken) : RefreshToken? {
        val id = (tokens.size+1).toLong()
        tokens[id] = RefreshTokenEntity.from(refreshToken)
        return tokens[id]?.toRefreshToken()
    }

}