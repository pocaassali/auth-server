package com.poc.authserver.users.infrastructure.api

import com.poc.authserver.users.core.application.ports.input.UserApplicationService
import org.springframework.stereotype.Component

@Component
class UserAdapter(
    private val userApplicationService: UserApplicationService
) {
    fun getAllUsers(): List<UserView> {
        return userApplicationService.getAllUsers().map { UserView.from(it) }
    }
}