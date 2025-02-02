package com.poc.authserver.core.application.dto.command

interface DeleteRefreshTokenCommand

class DeleteRefreshTokenByTokenCommand(val token: String) : DeleteRefreshTokenCommand
class DeleteRefreshTokenByUserIdCommand(val userId: String) : DeleteRefreshTokenCommand
