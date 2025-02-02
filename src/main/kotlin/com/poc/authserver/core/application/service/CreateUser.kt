package com.poc.authserver.core.application.service

import com.poc.authserver.core.application.dto.command.CreateUserCommand
import com.poc.authserver.core.application.ports.output.Users
import com.poc.authserver.core.domain.model.User

class CreateUser(
    private val users: Users
): AbstractCommandHandler<CreateUserCommand, User?>() {
    override fun execute(command: CreateUserCommand): User? {
        return users.save(command.toUser())
    }
}