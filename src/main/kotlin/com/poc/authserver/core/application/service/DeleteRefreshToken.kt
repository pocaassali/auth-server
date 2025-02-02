package com.poc.authserver.core.application.service

import com.poc.authserver.core.application.dto.command.DeleteRefreshTokenByTokenCommand
import com.poc.authserver.core.application.dto.command.DeleteRefreshTokenByUserIdCommand
import com.poc.authserver.core.application.dto.command.DeleteRefreshTokenCommand
import com.poc.authserver.core.application.ports.output.Tokens
import com.poc.authserver.core.domain.valueobject.Token
import java.util.*

class DeleteRefreshToken(private val tokens : Tokens) : AbstractCommandHandler<DeleteRefreshTokenCommand, Unit>() {
    override fun execute(command: DeleteRefreshTokenCommand) {
        when(command){
            is DeleteRefreshTokenByUserIdCommand -> tokens.delete(UUID.fromString(command.userId))
            is DeleteRefreshTokenByTokenCommand -> tokens.delete(Token(command.token))
        }
    }
}
