package com.poc.authserver.infrastructure.api.auth

import com.poc.authserver.core.application.dto.command.DeleteRefreshTokenCommand
import com.poc.authserver.core.application.dto.query.GetUserByIdQuery
import com.poc.authserver.core.application.ports.input.AuthApplicationService
import com.poc.authserver.core.application.ports.input.UserApplicationService
import com.poc.authserver.utils.CustomUserDetailsService
import com.poc.authserver.utils.JwtUtil
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import java.time.Instant

@Component
class AuthAdapter(
    private val authApplicationService: AuthApplicationService,
    private val passwordEncoder: PasswordEncoder,
    private val jwtUtil: JwtUtil,
    private val customUserDetailsService: CustomUserDetailsService,
    private val userApplicationService: UserApplicationService,
) {
    fun login(request: LoginRequest): TokensResponse? {
        val user = authApplicationService.getUserByCredentials(request.toQuery())
        if (user != null) {
            if (passwordEncoder.matches(request.password, user.password.value)) {
                val customUserDetails = customUserDetailsService.loadUserByUsername(user.identifier.toString())
                val accessToken = jwtUtil.generateToken(customUserDetails)
                val refreshToken = jwtUtil.generateRefreshToken(customUserDetails)
                //TODO : delete previous tokens for user
                //TODO : save new refreshToken
                return TokensResponse(accessToken, refreshToken)
            }
        }
        return null
    }

    fun refreshToken(request: RefreshTokenRequest): TokensResponse? {
        val storedToken = authApplicationService.getToken(request.toQuery())

        println(storedToken)
        if (storedToken?.expirationDate?.isBefore(Instant.now()) == true) {
            println("expire")
            authApplicationService.deleteToken(DeleteRefreshTokenCommand(storedToken.token.value)) //TODO : improve this
        }

        val userFromToken = storedToken?.userIdentifier?.let { GetUserByIdQuery(id = it) }
            ?.let { userApplicationService.getUserById(it) }

        println(userFromToken)

        if (userFromToken != null) {
            val customUserDetails = customUserDetailsService.loadUserByUsername(userFromToken.identifier.toString())
            val accessToken = jwtUtil.generateToken(customUserDetails)
            return TokensResponse(accessToken, request.refreshToken)
        }

        return null
    }
}