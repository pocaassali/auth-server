package com.poc.authserver.infrastructure.api.auth

import com.poc.authserver.core.application.dto.command.DeleteRefreshTokenByTokenCommand
import com.poc.authserver.core.application.dto.command.DeleteRefreshTokenByUserIdCommand
import com.poc.authserver.core.application.dto.command.SaveRefreshTokenCommand
import com.poc.authserver.core.application.dto.query.GetUserByIdQuery
import com.poc.authserver.core.application.ports.input.AuthApplicationService
import com.poc.authserver.core.application.ports.input.UserApplicationService
import com.poc.authserver.utils.CustomUserDetailsService
import com.poc.authserver.utils.JwtSessionService
import com.poc.authserver.utils.JwtUtil
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import java.time.Instant
import java.util.*
import java.util.concurrent.TimeUnit

@Component
class AuthAdapter(
    private val authApplicationService: AuthApplicationService,
    private val passwordEncoder: PasswordEncoder,
    private val jwtUtil: JwtUtil,
    private val customUserDetailsService: CustomUserDetailsService,
    private val userApplicationService: UserApplicationService,
    private val jwtSessionService: JwtSessionService
) {
    /*fun login(request: LoginRequest): TokensResponse? {
        val user = authApplicationService.getUserByCredentials(request.toQuery())
        if (user != null) {
            if (passwordEncoder.matches(request.password, user.password.value)) {
                val customUserDetails = customUserDetailsService.loadUserByUsername(user.identifier.toString())
                val accessToken = jwtUtil.generateToken(customUserDetails)
                val refreshToken = jwtUtil.generateRefreshToken(customUserDetails)
                authApplicationService.deleteToken(DeleteRefreshTokenByUserIdCommand(user.identifier.toString()))
                val command = SaveRefreshTokenCommand(
                    refreshToken = refreshToken,
                    expiresIn = Instant.now().plusSeconds(7 * 86400),
                    userIdentifier = user.identifier.toString()
                )
                val storedRefreshToken = authApplicationService.saveRefreshToken(command)
                return TokensResponse(accessToken, storedRefreshToken?.token?.value ?: "")
            }
        }
        return null
    }*/

    fun loginSessionBased(request: LoginRequest): LoginResponse? {
        val user = authApplicationService.getUserByCredentials(request.toQuery())
        if (user != null) {
            if (passwordEncoder.matches(request.password, user.password.value)) {
                val customUserDetails = customUserDetailsService.loadUserByUsername(user.identifier.toString())
                val accessToken = jwtUtil.generateToken(customUserDetails)
                val refreshToken = jwtUtil.generateRefreshToken(customUserDetails)
                val sessionId = UUID.randomUUID().toString()

                //jwtSessionService.storeAccessToken(sessionId, accessToken)
                jwtSessionService.storeAccessToken(sessionId, accessToken)
                jwtSessionService.storeRefreshToken(sessionId, refreshToken)

                return LoginResponse(sessionId)
            }
        }
        return null
    }

    /*fun refreshToken(request: RefreshTokenRequest): TokensResponse? {
        val storedToken = authApplicationService.getToken(request.toQuery())

        println("STORED TOKEN $storedToken")
        if (storedToken?.expirationDate?.isBefore(Instant.now()) == true) {
            println("expire")
            authApplicationService.deleteToken(DeleteRefreshTokenByTokenCommand(storedToken.token.value))
        }
        else {
            val userFromToken = storedToken?.userIdentifier?.let { GetUserByIdQuery(id = it) }
                ?.let { userApplicationService.getUserById(it) }

            println(userFromToken)

            if (userFromToken != null) {
                val customUserDetails = customUserDetailsService.loadUserByUsername(userFromToken.identifier.toString())
                val accessToken = jwtUtil.generateToken(customUserDetails)
                return TokensResponse(accessToken, request.refreshToken)
            }
        }

        return null
    }*/

    fun refreshTokenSessionBased(sessionId: String) {
        //val storedToken = authApplicationService.getToken(request.toQuery())
        val storedToken = jwtSessionService.getRefreshToken(sessionId)

        println("STORED TOKEN $storedToken")
        if (storedToken?.let { jwtUtil.isTokenExpired(it) } == true) {
            println("expire")
            //authApplicationService.deleteToken(DeleteRefreshTokenByTokenCommand(storedToken.token.value))
            jwtSessionService.deleteTokens(sessionId)
        }
        else {
            /*val userFromToken = storedToken?.userIdentifier?.let { GetUserByIdQuery(id = it) }
                ?.let { userApplicationService.getUserById(it) }*/
            val userFromToken = storedToken?.let { jwtUtil.extractUsername(it) }
                ?.let { userApplicationService.getUserById(GetUserByIdQuery(id = UUID.fromString(it))) }


            if (userFromToken != null) {
                val customUserDetails = customUserDetailsService.loadUserByUsername(userFromToken.identifier.toString())
                val accessToken = jwtUtil.generateToken(customUserDetails)

                jwtSessionService.storeAccessToken(sessionId, accessToken)
            }
        }
    }

    /*fun logout(request: LogoutRequest) {
        //authApplicationService.deleteToken(DeleteRefreshTokenByUserIdCommand(request.userId))
    }*/

    fun logoutSessionBased(sessionId: String) {
        jwtSessionService.deleteTokens(sessionId)
    }
}