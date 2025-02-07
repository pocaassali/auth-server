package com.poc.authserver.infrastructure.api.auth

import com.poc.authserver.core.application.dto.query.GetUserByIdQuery
import com.poc.authserver.core.application.ports.input.AuthApplicationService
import com.poc.authserver.core.application.ports.input.UserApplicationService
import com.poc.authserver.utils.CustomUserDetailsService
import com.poc.authserver.utils.JwtSessionService
import com.poc.authserver.utils.JwtUtil
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import java.util.*

@Component
class AuthAdapter(
    private val authApplicationService: AuthApplicationService,
    private val passwordEncoder: PasswordEncoder,
    private val jwtUtil: JwtUtil,
    private val customUserDetailsService: CustomUserDetailsService,
    private val userApplicationService: UserApplicationService,
    private val jwtSessionService: JwtSessionService
) {

    fun loginSessionBased(request: LoginRequest): LoginResponse? {
        val user = authApplicationService.getUserByCredentials(request.toQuery())
        if (user != null) {
            //TODO: if user already log in other session refresh all tokens
            if (passwordEncoder.matches(request.password, user.password.value)) {
                println("generate session")
                val customUserDetails = customUserDetailsService.loadUserByUsername(user.identifier.toString())
                val accessToken = jwtUtil.generateToken(customUserDetails)
                val refreshToken = jwtUtil.generateRefreshToken(customUserDetails)
                val sessionId = UUID.randomUUID().toString()

                jwtSessionService.storeSession(sessionId, accessToken, refreshToken)

                return LoginResponse(sessionId)
            }
        }
        println("No session create")
        return null
    }

    fun refreshTokenSessionBased(sessionId: String) {
        val sessionData = jwtSessionService.getSession(sessionId) ?: return

        println("SESSION DATA $sessionData")

        val refreshToken = sessionData.accessToken
        if (jwtUtil.isTokenExpired(refreshToken)) {
            println("expire")
            jwtSessionService.removeSession(sessionId)
        }
        else {
            val userFromToken = userApplicationService
                .getUserById(GetUserByIdQuery(UUID.fromString(jwtUtil.extractUsername(refreshToken))))


            if (userFromToken != null) {
                val customUserDetails = customUserDetailsService.loadUserByUsername(userFromToken.identifier.toString())
                println("OLD TOKEN : ${sessionData.accessToken}")
                val accessToken = jwtUtil.generateToken(customUserDetails)
                jwtSessionService.storeSession(sessionId, accessToken, refreshToken)
                println("NEW TOKEN : ${jwtSessionService.getSession(sessionId)!!.accessToken}")
            }
        }
    }

    fun logoutSessionBased(sessionId: String) {
        jwtSessionService.removeSession(sessionId)
    }
}