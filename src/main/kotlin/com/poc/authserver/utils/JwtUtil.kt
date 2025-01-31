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
const val NO_SECRET = "noSecret"

@Component
class JwtUtil {

    @PostConstruct
    fun init(){
        println(
            if (secretKey == NO_SECRET) "⚠\uFE0F JWT Secret Not Loaded !" else "\uD83D\uDD11 JWT Secret Loaded from env"
        )
    }

    @Value("\${jwt.secret}") // Charge la clé secrète depuis application.yml
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
            .setExpiration(Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10h
            .signWith(getSignKey(), SignatureAlgorithm.HS256)
            .compact()
    }



    /**
     * Extrait tous les claims (données) du token JWT
     */
    fun extractAllClaims(token: String): Claims {
        return Jwts.parserBuilder()
            .setSigningKey(getSignKey())
            .build()
            .parseClaimsJws(token)
            .body
    }

    /**
     * Extrait une information spécifique des claims
     */
    fun <T> extractClaim(token: String, claimsResolver: (Claims) -> T): T {
        val claims = extractAllClaims(token)
        return claimsResolver(claims)
    }

    /**
     * Extrait le username (subject) du token
     */
    fun extractUsername(token: String): String {
        return extractClaim(token) { it.subject }
    }

    /**
     * Vérifie si le token est valide
     */
    fun isTokenValid(token: String, userDetails: UserDetails): Boolean {
        val username = extractUsername(token)
        return username == userDetails.username && !isTokenExpired(token)
    }

    /**
     * Vérifie si le token est expiré
     */
    fun isTokenExpired(token: String): Boolean {
        return extractClaim(token) { it.expiration }.before(Date())
    }
}
