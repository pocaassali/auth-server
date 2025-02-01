package com.poc.authserver.core.application.ports.input

import com.poc.authserver.core.application.dto.command.DeleteRefreshTokenCommand
import com.poc.authserver.core.application.dto.query.GetUserByCredentialsQuery
import com.poc.authserver.core.application.dto.query.GetRefreshTokenByTokenQuery
import com.poc.authserver.core.domain.model.RefreshToken
import com.poc.authserver.core.domain.model.User

interface AuthApplicationService {
    fun getUserByCredentials(query: GetUserByCredentialsQuery) : User?
    fun getToken(query: GetRefreshTokenByTokenQuery) : RefreshToken?
    fun deleteToken(command : DeleteRefreshTokenCommand)
}