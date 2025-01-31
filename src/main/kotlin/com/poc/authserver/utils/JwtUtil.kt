package com.poc.authserver.utils

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*
import javax.crypto.spec.SecretKeySpec
import java.util.Base64

const val ONE_HOUR = 60 * 60 * 1000
const val NO_SECRET = "noSecret"

@Component
class JwtUtil {

    @PostConstruct
    fun init(){
        println(
            if (secret == NO_SECRET) "⚠\uFE0F JWT Secret Not Loaded !" else "\uD83D\uDD11 JWT Secret Loaded from env"
        )
    }

    @Value("\${jwt.secret:noSecret}")
    private lateinit var secret: String

    fun generateToken(username: String): String {
        val now = Date()
        val expiration = Date(now.time + ONE_HOUR)

        return Jwts.builder()
            .setSubject(username)
            .setIssuedAt(now)
            .setExpiration(expiration)
            .signWith(getSecretKey(), SignatureAlgorithm.HS256)
            .compact()
    }

    fun validateToken(token: String): Boolean {
        return try {
            Jwts.parserBuilder().setSigningKey(getSecretKey()).build().parseClaimsJws(token)
            true
        } catch (e: Exception) {
            false
        }
    }

    fun extractUsername(token: String): String? {
        return Jwts.parserBuilder().setSigningKey(getSecretKey()).build().parseClaimsJws(token).body.subject
    }

    private fun getSecretKey() = SecretKeySpec(Base64.getDecoder().decode(secret), "HmacSHA256")
}
