package com.poc.authserver.infrastructure.persistence.entity

import com.poc.authserver.core.domain.model.User
import com.poc.authserver.core.domain.valueobject.Mail
import com.poc.authserver.core.domain.valueobject.Password
import com.poc.authserver.core.domain.valueobject.UserRole
import java.util.*

data class UserEntity(
    val identifier: String,
    val mail: String,
    val password: String,
    val role: String,
) {
    fun toUser(): User {
        return User(
            identifier = UUID.fromString(identifier),
            mail = Mail(mail),
            password = Password(password),
            role = UserRole.valueOf(role),
        )
    }

    companion object {
        fun from(user: User): UserEntity {
            return UserEntity(
                identifier = user.identifier.toString(),
                mail = user.mail.value,
                password = user.password.value,
                role = user.role.name
            )
        }
    }
}
