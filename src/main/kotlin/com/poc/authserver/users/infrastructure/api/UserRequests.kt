package com.poc.authserver.users.infrastructure.api

import com.poc.authserver.users.core.application.dto.command.CreateUserCommand
import com.poc.authserver.users.core.application.dto.command.UpdateUserCommand

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

data class UserEditionRequest(
    val mail : String,
    val password : String,
    val role : String,
) {
    fun toCommand(id: String) : UpdateUserCommand {
        return UpdateUserCommand(id, mail, password ,role)
    }
}