package com.poc.authserver.infrastructure.api.auth

import com.poc.authserver.core.application.dto.query.GetUserByCredentialsQuery
import com.poc.authserver.infrastructure.api.remote.RemoteUserLoginRequest


data class LoginRequest(val username: String, val password: String){
    fun toQuery() : GetUserByCredentialsQuery {
        return GetUserByCredentialsQuery(username, password)
    }

    fun toRemoteRequest() : RemoteUserLoginRequest {
        return RemoteUserLoginRequest(username)
    }
}