package com.poc.authserver.infrastructure.api.auth

import com.poc.authserver.core.application.ports.input.AuthApplicationService
import com.poc.authserver.utils.CustomUserDetailsService
import com.poc.authserver.utils.JwtUtil
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class AuthAdapter(
    private val authApplicationService: AuthApplicationService,
    private val passwordEncoder: PasswordEncoder,
    private val jwtUtil: JwtUtil,
    private val customUserDetailsService: CustomUserDetailsService,
) {
    fun login(request: LoginRequest): TokensResponse? {
        val user = authApplicationService.getUserByCredentials(request.toQuery())
        if (user != null) {
            if (passwordEncoder.matches(request.password,user.password.value)){
                val customUserDetails = customUserDetailsService.loadUserByUsername(user.identifier.toString())
                val accessToken = jwtUtil.generateToken(customUserDetails)
                val refreshToken = jwtUtil.generateRefreshToken(customUserDetails)
                return TokensResponse(accessToken, refreshToken)
            }
        }
        return null
    }
}