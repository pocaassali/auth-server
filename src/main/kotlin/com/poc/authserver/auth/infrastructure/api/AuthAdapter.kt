package com.poc.authserver.auth.infrastructure.api

import com.poc.authserver.users.core.application.ports.input.AuthApplicationService
import com.poc.authserver.users.infrastructure.api.UserView
import org.springframework.stereotype.Component

@Component
class AuthAdapter(
    private val authApplicationService: AuthApplicationService
) {
    fun login(request: LoginRequest): UserView? {
        return authApplicationService.getUserByCredentials(request.toQuery())?.let { UserView.from(it) }
    }
}