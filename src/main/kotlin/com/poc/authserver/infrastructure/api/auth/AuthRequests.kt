package com.poc.authserver.infrastructure.api.auth

import com.poc.authserver.core.application.dto.query.GetUserByCredentialsQuery


data class LoginRequest(val username: String, val password: String){
    fun toQuery() : GetUserByCredentialsQuery {
        return GetUserByCredentialsQuery(username, password)
    }
}