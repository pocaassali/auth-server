package com.poc.authserver.core.application.service

import com.poc.authserver.core.application.dto.command.UpdateUserCommand
import com.poc.authserver.core.application.ports.output.Users
import com.poc.authserver.core.domain.model.User

class UpdateUser(
    private val users: Users
): AbstractCommandHandler<UpdateUserCommand, User?>() {
    override fun execute(command: UpdateUserCommand): User? {
        return users.update(command.toUser())
    }
}
