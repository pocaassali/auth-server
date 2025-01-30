package com.poc.authserver.auth.infrastructure.api

import com.poc.authserver.users.core.application.dto.query.GetUserByCredentialsQuery

data class LoginRequest(val mail: String, val password: String){
    fun toQuery() : GetUserByCredentialsQuery {
        return GetUserByCredentialsQuery(mail, password)
    }
}