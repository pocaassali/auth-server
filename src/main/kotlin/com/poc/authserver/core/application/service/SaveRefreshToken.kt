package com.poc.authserver.core.application.service

import com.poc.authserver.core.application.dto.command.SaveRefreshTokenCommand
import com.poc.authserver.core.application.ports.output.Tokens
import com.poc.authserver.core.domain.model.RefreshToken

class SaveRefreshToken(private val tokens: Tokens) : AbstractCommandHandler<SaveRefreshTokenCommand, RefreshToken?>() {
    override fun execute(command: SaveRefreshTokenCommand) : RefreshToken? {
        return tokens.save(command.toRefreshToken())
    }
}