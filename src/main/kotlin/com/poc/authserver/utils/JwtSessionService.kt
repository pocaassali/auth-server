package com.poc.authserver.utils

import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Service
import java.time.Duration

const val SESSION_EXPIRATION = 7L

@Service
class JwtSessionService(private val redisTemplate: StringRedisTemplate) {

    fun storeSession(session: Session) : Session {
        redisTemplate.opsForHash<String, String>().putAll(session.id, session.data)
        redisTemplate.expire(session.id, Duration.ofDays(SESSION_EXPIRATION))
        return session
    }

    fun removeSession(sessionId: String) {
        redisTemplate.delete(sessionId)
    }

    fun getSession(sessionId: String): Session? {
        val sessionData = redisTemplate.opsForHash<String, String>().entries(sessionId)
        return if (sessionData.isNotEmpty()) {
            Session(sessionId, sessionData)
        } else {
            null
        }
    }
}