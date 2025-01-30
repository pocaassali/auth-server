package com.poc.authserver.infrastructure.api.auth

import com.poc.authserver.core.application.ports.input.AuthApplicationService
import com.poc.authserver.infrastructure.api.user.UserView
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class AuthAdapter(
    private val authApplicationService: AuthApplicationService,
    private val passwordEncoder: PasswordEncoder,
) {
    fun login(request: LoginRequest): UserView? {
        val user = authApplicationService.getUserByCredentials(request.toQuery())
        if (user != null) {
            if (passwordEncoder.matches(request.password,user.password.value)){
                return UserView.from(user)
            }
        }
        return null
    }
}