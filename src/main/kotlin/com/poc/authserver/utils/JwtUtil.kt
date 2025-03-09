package com.poc.authserver.utils

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.security.Key
import java.util.*

const val ONE_HOUR = 60 * 60 * 1000
const val ONE_WEEK = 7 * 24 * 60 * 60 * 1000
const val NO_SECRET = "noSecret"

@Component
class JwtUtil {

    @PostConstruct
    fun init(){
        println(
            if (secretKey == NO_SECRET) "⚠\uFE0F JWT Secret Not Loaded !" else "\uD83D\uDD11 JWT Secret Loaded from env"
        )
    }

    @Value("\${jwt.secret}")
    private lateinit var secretKey: String

    private fun getSignKey(): Key {
        val keyBytes = Decoders.BASE64.decode(secretKey)
        return Keys.hmacShaKeyFor(keyBytes)
    }

    fun generateToken(userDetails: CustomUserDetails): String {
        return Jwts.builder()
            .setSubject(userDetails.username)
            .claim("roles", userDetails.authorities.map { it.authority })
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + 60*1000))
            .signWith(getSignKey(), SignatureAlgorithm.HS256)
            .compact()
    }

    fun generateRefreshToken(user: CustomUserDetails): String {
        return Jwts.builder()
            .setSubject(user.username)
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + ONE_WEEK))
            .signWith(getSignKey(), SignatureAlgorithm.HS256)
            .compact()
    }

    fun extractAllClaims(token: String): Claims {
        return Jwts.parserBuilder()
            .setSigningKey(getSignKey())
            .build()
            .parseClaimsJws(token) //An error is thrown here when given access token is expired
            .body
    }

    fun <T> extractClaim(token: String, claimsResolver: (Claims) -> T): T {
        val claims = extractAllClaims(token)
        return claimsResolver(claims)
    }

    fun extractUsername(token: String): String {
        return extractClaim(token) { it.subject }
    }

    fun isTokenValid(token: String, userDetails: UserDetails): Boolean {
        val username = extractUsername(token)
        return username == userDetails.username && !isTokenExpired(token)
    }

    fun isTokenExpired(token: String): Boolean {
        return extractClaim(token) { it.expiration }.before(Date())
    }
}
