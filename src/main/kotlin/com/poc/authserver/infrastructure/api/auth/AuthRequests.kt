package com.poc.authserver.infrastructure.api.auth

import com.poc.authserver.core.application.dto.query.GetUserByCredentialsQuery
import com.poc.authserver.core.application.dto.query.GetRefreshTokenByTokenQuery


data class LoginRequest(val username: String, val password: String){
    fun toQuery() : GetUserByCredentialsQuery {
        return GetUserByCredentialsQuery(username, password)
    }
}

data class RefreshTokenRequest(val refreshToken: String){
    fun toQuery() : GetRefreshTokenByTokenQuery {
        return GetRefreshTokenByTokenQuery(refreshToken)
    }
}

data class LogoutRequest(val userId: String, val refreshToken: String = ""){}