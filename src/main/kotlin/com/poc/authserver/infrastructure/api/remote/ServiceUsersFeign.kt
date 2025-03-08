package com.poc.authserver.infrastructure.api.remote

import com.poc.authserver.infrastructure.api.user.UserView
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

interface ServiceUsersFeign {
    @PostMapping("/users")
    fun registerUser(@RequestBody request : RemoteUserRegisterRequest) : RemoteRegisterResponse?

    @PostMapping("/users/credentials")
    fun getUserByCredentials(@RequestBody request : RemoteUserLoginRequest) : RemoteLoginResponse?
}

data class RemoteUserRegisterRequest(
    val mail: String,
    val password: String,
    val role: String = "USER"
)

data class RemoteUserLoginRequest(
    val mail: String,
)

data class RemoteRegisterResponse(
    val identifier: String,
    val mail: String,
    val role: String,
){
    fun toUserView() : UserView {
        return UserView(identifier, mail, role)
    }
}

data class RemoteLoginResponse(
    val identifier: String,
    val password: String,
    val mail: String,
    val role: String,
){
    fun toUserView() : UserView {
        return UserView(identifier, mail, role)
    }
}