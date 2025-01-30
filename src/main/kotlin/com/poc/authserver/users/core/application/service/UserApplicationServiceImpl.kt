package com.poc.authserver.users.core.application.service

import com.poc.authserver.users.core.application.dto.command.CreateUserCommand
import com.poc.authserver.users.core.application.dto.command.DeleteUserCommand
import com.poc.authserver.users.core.application.dto.command.UpdateUserCommand
import com.poc.authserver.users.core.application.dto.query.GetAllUsersQuery
import com.poc.authserver.users.core.application.dto.query.GetUserByIdQuery
import com.poc.authserver.users.core.application.ports.input.UserApplicationService
import com.poc.authserver.users.core.domain.model.User

class UserApplicationServiceImpl(
    private val createUser : CreateUser,
    private val getAllUsers : GetAllUsers,
    private val getUserById : GetUserById,
    private val updateUser : UpdateUser,
    private val deleteUser : DeleteUser,
) : UserApplicationService {
    override fun getAllUsers(): List<User> {
        return getAllUsers.handle(GetAllUsersQuery())
    }

    override fun getUserById(query: GetUserByIdQuery): User? {
        return getUserById.handle(query)
    }

    override fun createUser(command: CreateUserCommand): User? {
        return createUser.handle(command)
    }

    override fun updateUser(command: UpdateUserCommand): User? {
        return updateUser.handle(command)
    }

    override fun deleteUser(command: DeleteUserCommand) {
        deleteUser.handle(command)
        return
    }
}