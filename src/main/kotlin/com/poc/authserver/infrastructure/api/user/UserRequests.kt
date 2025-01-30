package com.poc.authserver.infrastructure.api.user

import com.poc.authserver.core.application.dto.command.CreateUserCommand
import com.poc.authserver.core.application.dto.command.UpdateUserCommand


data class UserCreationRequest(
    val identifier : String,
    val mail : String,
    val password : String,
    val role : String,
) {
    fun toCommand(encryptedPassword : String) : CreateUserCommand {
        return CreateUserCommand(
            identifier = identifier,
            mail = mail,
            password = password,
            encryptedPassword = encryptedPassword,
            role = role,
        )
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