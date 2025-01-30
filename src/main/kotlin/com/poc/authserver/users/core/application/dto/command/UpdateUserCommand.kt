package com.poc.authserver.users.core.application.dto.command

import com.poc.authserver.users.core.domain.model.User
import com.poc.authserver.users.core.domain.valueobject.Mail
import com.poc.authserver.users.core.domain.valueobject.Password
import com.poc.authserver.users.core.domain.valueobject.UserRole
import java.util.*

class UpdateUserCommand(
    val identifier : String,
    val mail : String,
    val password : String,
    val role : String,
) {

    fun toUser(): User {
        return User(
            identifier = UUID.fromString(identifier),
            mail = Mail(mail),
            password = Password(password),
            role = UserRole.valueOf(role),
        )
    }

}
