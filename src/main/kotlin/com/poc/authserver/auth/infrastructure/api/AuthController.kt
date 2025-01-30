package com.poc.authserver.auth.infrastructure.api

import com.poc.authserver.users.infrastructure.api.UserView
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