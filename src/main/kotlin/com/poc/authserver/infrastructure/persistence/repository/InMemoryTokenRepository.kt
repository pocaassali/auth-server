package com.poc.authserver.infrastructure.persistence.repository

import com.poc.authserver.core.application.ports.output.Tokens
import com.poc.authserver.core.domain.model.RefreshToken
import com.poc.authserver.core.domain.valueobject.Token
import com.poc.authserver.infrastructure.persistence.entity.RefreshTokenEntity
import com.poc.authserver.infrastructure.persistence.entity.UserEntity
import org.springframework.stereotype.Repository
import java.time.Instant
import java.util.*

@Repository
class InMemoryTokenRepository : Tokens {

    private val tokens = mutableMapOf<Long,RefreshTokenEntity>(
        /*Pair(1L, RefreshTokenEntity(
            token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJiZDY1NzE2OC1lNTczLTQ5MjUtOTAwYS1kNWYyNmU4Mjc2MGIiLCJpYXQiOjE3Mzg0MTkzOTksImV4cCI6MTczOTAyNDE5OX0.kkyeOkeGNMBKQW8hNxTxDklqUYFwgq9J4lsOczTjG0E",
            userId = "bd657168-e573-4925-900a-d5f26e82760b",
            expiredAt = Instant.now().plusSeconds(7 * 86400)
        )),*/
    )

    override fun findByToken(token: Token): RefreshToken? {
        return tokens.values.find { it.token == token.value }?.toRefreshToken()
    }

    override fun delete(token: Token) {
        println("AVANT SUPPRESSION PAR TOKEN $tokens")
        val tokenToDelete = tokens.entries.find { it.value.token == token.value }
        tokenToDelete?.let { tokens.remove(it.key) }
        println("APRES SUPPRESSION PAR TOKEN $tokens")
    }

    override fun delete(userId: UUID) {
        println("AVANT SUPPRESSION PAR USERID $tokens")
        val tokenToDelete = tokens.entries.find { it.value.userId == userId.toString() }
        tokenToDelete?.let { tokens.remove(it.key) }
        println("APRES SUPPRESSION PAR USERID $tokens")
    }

    override fun save(refreshToken: RefreshToken) : RefreshToken? {
        println("AVANT SAVE $tokens")
        val id = (tokens.size+1).toLong()
        tokens[id] = RefreshTokenEntity.from(refreshToken)
        println("APRES SAVE $tokens")
        return tokens[id]?.toRefreshToken()
    }

}