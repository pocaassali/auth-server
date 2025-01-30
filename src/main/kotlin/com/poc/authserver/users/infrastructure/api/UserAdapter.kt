package com.poc.authserver.users.infrastructure.api

import com.poc.authserver.users.core.application.dto.query.GetUserByIdQuery
import com.poc.authserver.users.core.application.ports.input.UserApplicationService
import org.springframework.stereotype.Component
import java.util.*

@Component
class UserAdapter(
    private val userApplicationService: UserApplicationService
) {
    fun getAllUsers(): List<UserView> {
        return userApplicationService.getAllUsers().map { UserView.from(it) }
    }

    fun getUserById(id: String): UserView? {
        return userApplicationService.getUserById(GetUserByIdQuery(UUID.fromString(id)))?.let { UserView.from(it) }
    }

    fun create(request: UserCreationRequest): UserView? {
        return userApplicationService.createUser(request.toCommand())?.let { UserView.from(it) }
    }
}