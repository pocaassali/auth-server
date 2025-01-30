package com.poc.authserver.infrastructure.api.auth

import com.poc.authserver.core.application.ports.input.AuthApplicationService
import com.poc.authserver.infrastructure.api.user.UserView
import org.springframework.stereotype.Component

@Component
class AuthAdapter(
    private val authApplicationService: AuthApplicationService
) {
    fun login(request: LoginRequest): UserView? {
        return authApplicationService.getUserByCredentials(request.toQuery())?.let { UserView.from(it) }
    }
}