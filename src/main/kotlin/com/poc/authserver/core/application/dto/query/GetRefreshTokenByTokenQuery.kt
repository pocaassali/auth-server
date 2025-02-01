package com.poc.authserver.core.application.dto.query

import com.poc.authserver.core.domain.valueobject.Token


data class GetRefreshTokenByTokenQuery(
    val token: String,
){
    fun toToken() : Token{
        return Token(token)
    }
}
