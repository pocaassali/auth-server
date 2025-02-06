package com.poc.authserver.infrastructure.api.user

import com.poc.authserver.core.application.dto.command.CreateUserCommand
import com.poc.authserver.core.application.dto.command.UpdateUserCommand


//TODO: improve request by removing identifier and role may be add username latter for now only mail and password are required
data class UserCreationRequest(
    val identifier : String = "3d41a343-3c56-4462-9f80-97b5eabb82fa",
    val mail : String,
    val password : String,
    val role : String = "USER",
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
    fun toCommand(id: String, encryptedPassword : String) : UpdateUserCommand {
        return UpdateUserCommand(
            identifier = id,
            mail = mail,
            password = password,
            hashedPassword = encryptedPassword,
            role = role
        )
    }
}