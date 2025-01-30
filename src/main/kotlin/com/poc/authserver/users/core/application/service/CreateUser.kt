package com.poc.authserver.users.core.application.service

import com.poc.authserver.users.core.application.dto.command.CreateUserCommand
import com.poc.authserver.users.core.application.ports.output.Users
import com.poc.authserver.users.core.domain.model.User

class CreateUser(
    private val users: Users
): AbstractCommandHandler<CreateUserCommand, User?>() {
    override fun execute(command: CreateUserCommand): User? {
        return users.save(command.toUser())
    }
}