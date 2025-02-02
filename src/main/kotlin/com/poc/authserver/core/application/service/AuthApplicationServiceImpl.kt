package com.poc.authserver.core.application.service

import com.poc.authserver.core.application.dto.command.DeleteRefreshTokenByTokenCommand
import com.poc.authserver.core.application.dto.command.DeleteRefreshTokenByUserIdCommand
import com.poc.authserver.core.application.dto.command.DeleteRefreshTokenCommand
import com.poc.authserver.core.application.dto.command.SaveRefreshTokenCommand
import com.poc.authserver.core.application.dto.query.GetUserByCredentialsQuery
import com.poc.authserver.core.application.dto.query.GetRefreshTokenByTokenQuery
import com.poc.authserver.core.application.ports.input.AuthApplicationService
import com.poc.authserver.core.domain.model.RefreshToken
import com.poc.authserver.core.domain.model.User

class AuthApplicationServiceImpl(
    private val getUserByCredentials: GetUserByCredentials,
    private val getRefreshTokenByToken: GetRefreshTokenByToken,
    private val deleteRefreshToken: DeleteRefreshToken,
    private val saveRefreshToken: SaveRefreshToken,
) : AuthApplicationService {

    override fun getUserByCredentials(query: GetUserByCredentialsQuery) : User? {
        return getUserByCredentials.handle(query)
    }

    override fun getToken(query: GetRefreshTokenByTokenQuery): RefreshToken? {
        return getRefreshTokenByToken.handle(query)
    }

    override fun deleteToken(command: DeleteRefreshTokenCommand) {
        deleteRefreshToken.handle(command)
    }

    override fun saveRefreshToken(command: SaveRefreshTokenCommand) : RefreshToken? {
        return saveRefreshToken.handle(command)
    }

    /*override fun deleteToken(command: DeleteRefreshTokenByTokenCommand) {
        deleteRefreshToken.handle(command)
    }

    override fun deleteTokenForUser(command: DeleteRefreshTokenByUserIdCommand) {
        deleteRefreshToken.handle(command)
    }*/

}