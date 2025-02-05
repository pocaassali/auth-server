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

    /*fun loginSessionBased(request: LoginRequest): LoginResponse? {
        val user = authApplicationService.getUserByCredentials(request.toQuery())
        if (user != null) {
            if (passwordEncoder.matches(request.password, user.password.value)) {
                val customUserDetails = customUserDetailsService.loadUserByUsername(user.identifier.toString())
                val accessToken = jwtUtil.generateToken(customUserDetails)
                val refreshToken = jwtUtil.generateRefreshToken(customUserDetails)
                //val sessionId = UUID.randomUUID().toString()

                //jwtSessionService.storeAccessToken(sessionId, accessToken)
                jwtSessionService.storeAccessToken(user.identifier.toString(), accessToken)
                jwtSessionService.storeRefreshToken(user.identifier.toString(), refreshToken)

                return LoginResponse(user.identifier.toString())
            }
        }
        return null
    }*/

    fun loginSessionBased(request: LoginRequest): LoginResponse? {
        println(request)
        val user = authApplicationService.getUserByCredentials(request.toQuery())
        if (user != null) {
            if (passwordEncoder.matches(request.password, user.password.value)) {
                println("generate session")
                val customUserDetails = customUserDetailsService.loadUserByUsername(user.identifier.toString())
                val accessToken = jwtUtil.generateToken(customUserDetails)
                val refreshToken = jwtUtil.generateRefreshToken(customUserDetails)
                val sessionId = UUID.randomUUID().toString()

                //jwtSessionService.storeAccessToken(sessionId, accessToken)
                //jwtSessionService.storeAccessToken(sessionId, accessToken)
                //jwtSessionService.storeRefreshToken(sessionId, refreshToken)

                jwtSessionService.storeSession(sessionId, accessToken, refreshToken)

                return LoginResponse(sessionId)
            }
        }
        println("No session create")
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

    /*fun refreshTokenSessionBased(sessionId: String) {
        //val storedToken = authApplicationService.getToken(request.toQuery())
        val storedToken = jwtSessionService.getRefreshToken(sessionId)

        println("STORED TOKEN $storedToken")
        if (storedToken?.let { jwtUtil.isTokenExpired(it) } == true) {
            println("expire")
            //authApplicationService.deleteToken(DeleteRefreshTokenByTokenCommand(storedToken.token.value))
            jwtSessionService.deleteTokens(sessionId)
            //TODO : not 200 response
        }
        else {
            *//*val userFromToken = storedToken?.userIdentifier?.let { GetUserByIdQuery(id = it) }
                ?.let { userApplicationService.getUserById(it) }*//*
            val userFromToken = storedToken?.let { jwtUtil.extractUsername(it) }
                ?.let { userApplicationService.getUserById(GetUserByIdQuery(id = UUID.fromString(it))) }


            if (userFromToken != null) {
                val customUserDetails = customUserDetailsService.loadUserByUsername(userFromToken.identifier.toString())
                val accessToken = jwtUtil.generateToken(customUserDetails)

                jwtSessionService.storeAccessToken(sessionId, accessToken)
            }
        }
    }*/

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

    /*fun logout(request: LogoutRequest) {
        //authApplicationService.deleteToken(DeleteRefreshTokenByUserIdCommand(request.userId))
    }*/

    fun logoutSessionBased(sessionId: String) {
        jwtSessionService.removeSession(sessionId)
    }
}