package com.poc.authserver.users.infrastructure.api

import com.poc.authserver.users.core.application.dto.command.CreateUserCommand

data class UserCreationRequest(
    val identifier : String,
    val mail : String,
    val password : String,
    val role : String,
) {
    fun toCommand() : CreateUserCommand {
        return CreateUserCommand(identifier, mail, password ,role)
    }
}