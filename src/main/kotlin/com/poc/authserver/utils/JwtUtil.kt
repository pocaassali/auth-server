package com.poc.authserver.utils

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
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

    /*fun generateToken(userIdentifier: String): String {
        val now = Date()
        val expiration = Date(now.time + ONE_HOUR)

        return Jwts.builder()
            .setSubject(userIdentifier)
            .setIssuedAt(now)
            .setExpiration(expiration)
            .signWith(getSecretKey(), SignatureAlgorithm.HS256)
            .compact()
    }*/

    fun generateToken(userDetails: UserDetails): String {
        return Jwts.builder()
            .setSubject(userDetails.username)
            .claim("roles", userDetails.authorities.map { it.authority })
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10h
            .signWith(getSecretKey(), SignatureAlgorithm.HS256)
            .compact()
    }

    fun validateToken(token: String, userDetails: UserDetails): Boolean {
        val username = extractUsername(token)
        return username == userDetails.username && !isTokenExpired(token)
    }

    fun extractUsername(token: String): String? {
        return Jwts.parserBuilder().setSigningKey(getSecretKey()).build().parseClaimsJws(token).body.subject
    }

    private fun isTokenExpired(token: String): Boolean {
        return getClaims(token).expiration.before(Date())
    }

    private fun getClaims(token: String): Claims {
        return Jwts.parserBuilder()
            .setSigningKey(getSecretKey())
            .build()
            .parseClaimsJws(token)
            .body
    }

    private fun getSecretKey() = SecretKeySpec(Base64.getDecoder().decode(secret), "HmacSHA256")
}
