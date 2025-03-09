package com.poc.authserver.infrastructure.api.auth

import com.poc.authserver.core.application.dto.query.GetUserByIdQuery
import com.poc.authserver.core.application.ports.input.AuthApplicationService
import com.poc.authserver.core.application.ports.input.UserApplicationService
import com.poc.authserver.infrastructure.api.remote.RemoteLoginResponse
import com.poc.authserver.infrastructure.api.remote.ServiceUsersFeign
import com.poc.authserver.utils.CustomUserDetails
import com.poc.authserver.utils.CustomUserDetailsService
import com.poc.authserver.utils.JwtSessionService
import com.poc.authserver.utils.JwtUtil
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import java.util.*

@Component
class AuthAdapter(
    private val passwordEncoder: PasswordEncoder,
    private val jwtUtil: JwtUtil,
    private val jwtSessionService: JwtSessionService,
    private val serviceUsersFeign: ServiceUsersFeign,
) {

    fun loginSessionBased(request: LoginRequest): LoginResponse? {
        val user = serviceUsersFeign.getUserByCredentials(request.toRemoteRequest())
        if (user != null) {
            //TODO: if user already log in other session refresh all tokens
            if (passwordEncoder.matches(request.password, user.password)) {
                println("generate session")
                val customUserDetails = CustomUserDetails(
                    userId = user.identifier,
                    username = user.identifier,
                    password = user.password,
                    authorities = listOf(SimpleGrantedAuthority("ROLE_${user.role}"))
                )
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

        val refreshToken = sessionData.refreshToken
        if (jwtUtil.isTokenExpired(refreshToken)) {
            println("expire")
            jwtSessionService.removeSession(sessionId)
        }
        else {

            val userFromToken = serviceUsersFeign
                .getUserByIdentifier(jwtUtil.extractUsername(sessionData.refreshToken))

            println("USER FROM TOKEN => $userFromToken")

            if (userFromToken != null) {

                val customUserDetails = CustomUserDetails(
                    userId = userFromToken.identifier,
                    username = userFromToken.identifier,
                    password = userFromToken.password,
                    authorities = listOf(SimpleGrantedAuthority("ROLE_${userFromToken.role}"))
                )
                val accessToken = jwtUtil.generateToken(customUserDetails)
                jwtSessionService.storeSession(sessionId, accessToken, refreshToken)
            }
        }
    }

    fun logoutSessionBased(sessionId: String) {
        jwtSessionService.removeSession(sessionId)
    }
}