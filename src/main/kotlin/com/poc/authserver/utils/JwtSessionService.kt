package com.poc.authserver.utils

import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Service
import java.time.Duration

const val SESSION_EXPIRATION = 7L

@Service
class JwtSessionService(private val redisTemplate: StringRedisTemplate) {

    fun storeSession(sessionId: String, accessToken: String, refreshToken: String) {
        val sessionData = mapOf(
            "accessToken" to accessToken,
            "refreshToken" to refreshToken
        )
        redisTemplate.opsForHash<String, String>().putAll(sessionId, sessionData)
        redisTemplate.expire(sessionId, Duration.ofDays(SESSION_EXPIRATION))
    }

    fun removeSession(sessionId: String) {
        redisTemplate.delete(sessionId)
    }

    fun getSession(sessionId: String): SessionData? {
        val sessionData = redisTemplate.opsForHash<String, String>().entries(sessionId)
        return if (sessionData.isNotEmpty()) {
            SessionData(sessionData["accessToken"] ?: "", sessionData["refreshToken"] ?: "")
        } else {
            null
        }
    }
}

data class SessionData(val accessToken: String, val refreshToken: String)