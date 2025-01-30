package com.poc.authserver.infrastructure.api.auth

import com.poc.authserver.infrastructure.api.user.UserView
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/svc/auth")
class AuthController(
    private val authAdapter: AuthAdapter
) {

    @PostMapping("/login")
    fun loginWithCredentials(@RequestBody request: LoginRequest) : ResponseEntity<UserView> {
        return ResponseEntity.ok(authAdapter.login(request))
    }
}