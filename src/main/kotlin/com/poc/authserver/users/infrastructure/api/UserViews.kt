package com.poc.authserver.users.infrastructure.api

import com.poc.authserver.users.core.domain.model.User

data class UserView(
    val identifier : String,
    val mail : String,
    val role : String,
) {
    companion object {
        fun from(user: User) : UserView {
            return UserView(
                identifier = user.identifier.toString(),
                mail = user.mail.value,
                role = user.role.name
            )
        }
    }
}