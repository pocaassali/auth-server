package com.poc.authserver.infrastructure.api.user

import com.poc.authserver.core.application.dto.command.DeleteUserCommand
import com.poc.authserver.core.application.dto.query.GetUserByIdQuery
import com.poc.authserver.core.application.ports.input.UserApplicationService
import com.poc.authserver.infrastructure.api.remote.RemoteUserRegisterRequest
import com.poc.authserver.infrastructure.api.remote.ServiceUsersFeign
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import java.util.*

@Component
class UserAdapter(
    private val userApplicationService: UserApplicationService,
    private val passwordEncoder: PasswordEncoder,
    private val serviceUsersFeign: ServiceUsersFeign,
) {
    fun getAllUsers(): List<UserView> {
        return userApplicationService.getAllUsers().map { UserView.from(it) }
    }

    fun getUserById(id: String): UserView? {
        return userApplicationService.getUserById(GetUserByIdQuery(UUID.fromString(id)))?.let { UserView.from(it) }
    }

    fun create(request: UserCreationRequest): UserView? {
        val remoteUserRegisterRequest = RemoteUserRegisterRequest(request.mail, request.password)
        return try {
            serviceUsersFeign
                .registerUser(remoteUserRegisterRequest)
                ?.toUserView()
        } catch (e : Exception){
            println(e.message)
            null
        }
    }

    fun update(id: String, request: UserEditionRequest): UserView? {
        return userApplicationService.updateUser(
            request.toCommand(id, passwordEncoder.encode(request.password))
        )?.let { UserView.from(it) }
    }

    fun delete(id: String) {
        userApplicationService.deleteUser(DeleteUserCommand(id))
    }
}