package com.poc.authserver.core.application.service

import com.poc.authserver.core.application.dto.command.DeleteUserCommand
import com.poc.authserver.core.application.ports.output.Users
import java.util.*

class DeleteUser(private val users : Users) : AbstractCommandHandler<DeleteUserCommand, Unit>() {
    override fun execute(command: DeleteUserCommand) {
        users.delete(UUID.fromString(command.id))
    }

}
