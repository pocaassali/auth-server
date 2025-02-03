package com.poc.authserver.utils

import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit

const val ACCESS_TOKEN_SESSION_KEY = "ACCESS_TOKEN"
const val DEFAULT_ACCESS_TOKEN_EXPIRATION = 1L
const val REFRESH_TOKEN_SESSION_KEY = "REFRESH_TOKEN"
const val DEFAULT_REFRESH_TOKEN_EXPIRATION = 7L

@Service
class JwtSessionService(private val redisTemplate: StringRedisTemplate) {

    /**
     * For 1 hour by default
     * */
    fun storeAccessToken(
        sessionId: String,
        token: String,
        expiration: Long = DEFAULT_ACCESS_TOKEN_EXPIRATION,
        timeUnit: TimeUnit = TimeUnit.HOURS
    ) = redisTemplate.opsForValue().set("$ACCESS_TOKEN_SESSION_KEY:$sessionId", token, expiration, timeUnit)


    /**
     * For 1 week by default
     * */
    fun storeRefreshToken(
        sessionId: String,
        token: String,
        expiration: Long = DEFAULT_REFRESH_TOKEN_EXPIRATION,
        timeUnit: TimeUnit = TimeUnit.DAYS
    ) = redisTemplate.opsForValue().set("$REFRESH_TOKEN_SESSION_KEY:$sessionId", token, expiration, timeUnit)


    fun getAccessToken(sessionId: String): String? {
        return redisTemplate.opsForValue().get("$ACCESS_TOKEN_SESSION_KEY:$sessionId")
    }

    fun getRefreshToken(sessionId: String): String? {
        return redisTemplate.opsForValue().get("$REFRESH_TOKEN_SESSION_KEY:$sessionId")
    }

    /*fun invalidateSession(sessionId: String) {
        redisTemplate.delete("session:$sessionId")
    }*/

    fun deleteTokens(sessionId: String) {
        redisTemplate.delete("$ACCESS_TOKEN_SESSION_KEY:$sessionId")
        redisTemplate.delete("$REFRESH_TOKEN_SESSION_KEY:$sessionId")
    }
}