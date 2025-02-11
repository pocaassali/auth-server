package com.poc.authserver.infrastructure.api.auth

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthController(
    private val authAdapter: AuthAdapter
) {

    @PostMapping("/login")
    fun loginWithCredentials(@RequestBody request: LoginRequest) : ResponseEntity<TokensResponse?> {
        return ResponseEntity.ok(authAdapter.login(request))
    }

    @PostMapping("/refresh")
    fun refreshToken(@RequestBody request: RefreshTokenRequest): ResponseEntity<TokensResponse?> {
        return ResponseEntity.ok(authAdapter.refreshToken(request)) //if adapter return null return 401 here
    }

    @PostMapping("/logout")
    fun logout(@RequestBody request : LogoutRequest) : ResponseEntity<String> {
        authAdapter.logout(request)
        return ResponseEntity.ok("User with id ${request.userId} successfully logged out")
    }
}