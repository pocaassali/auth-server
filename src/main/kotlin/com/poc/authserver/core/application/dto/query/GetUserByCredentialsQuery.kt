package com.poc.authserver.core.application.dto.query

import com.poc.authserver.core.domain.valueobject.Credentials

data class GetUserByCredentialsQuery(
    val mail: String,
    val password: String
){
    fun toCredentials() : Credentials {
        return Credentials(mail, password)
    }
}
