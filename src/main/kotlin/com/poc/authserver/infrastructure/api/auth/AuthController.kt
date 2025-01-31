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
    fun loginWithCredentials(@RequestBody request: LoginRequest) : ResponseEntity<AccessTokenResponse?> {
        return ResponseEntity.ok(authAdapter.login(request))
    }
}