package com.poc.authserver.utils

import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit

@Service
class JwtSessionService(private val redisTemplate: StringRedisTemplate) {
    fun storeJwt(sessionId: String, jwt: String, expiration: Long) {
        redisTemplate.opsForValue().set("session:$sessionId", jwt, expiration, TimeUnit.MILLISECONDS)
    }

    fun getJwt(sessionId: String): String? {
        return redisTemplate.opsForValue().get("session:$sessionId")
    }

    fun invalidateSession(sessionId: String) {
        redisTemplate.delete("session:$sessionId")
    }
}