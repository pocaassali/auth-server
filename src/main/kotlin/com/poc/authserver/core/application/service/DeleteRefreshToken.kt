package com.poc.authserver.core.application.service

import com.poc.authserver.core.application.dto.command.DeleteRefreshTokenCommand
import com.poc.authserver.core.application.ports.output.Tokens
import com.poc.authserver.core.domain.valueobject.Token

class DeleteRefreshToken(private val tokens : Tokens) : AbstractCommandHandler<DeleteRefreshTokenCommand, Unit>() {
    override fun execute(command: DeleteRefreshTokenCommand) {
        tokens.delete(Token(command.token))
    }
}
