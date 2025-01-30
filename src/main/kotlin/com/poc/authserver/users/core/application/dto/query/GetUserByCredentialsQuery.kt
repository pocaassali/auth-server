package com.poc.authserver.users.core.application.dto.query

import com.poc.authserver.users.core.domain.valueobject.Credentials

data class GetUserByCredentialsQuery(
    val mail: String,
    val password: String
){
    fun toCredentials() : Credentials {
        return Credentials(mail, password)
    }
}
