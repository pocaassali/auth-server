package com.poc.authserver.controller.remote

import org.springframework.web.bind.annotation.*

interface ServiceUsersFeign {
    @PostMapping("/users")
    fun registerUser(@RequestBody request : RemoteUserRegisterRequest) : RemoteRegisterResponse?

    @PostMapping("/users/credentials")
    fun getUserByCredentials(@RequestBody request : RemoteUserLoginRequest) : RemoteLoginResponse?

    @GetMapping("/users/{id}")
    fun getUserByIdentifier(@PathVariable id: String) : RemoteUser?
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
        return UserView(
            identifier = identifier,
            mail = mail,
            role = role,
        )
    }
}

data class RemoteLoginResponse(
    val identifier: String,
    val password: String,
    val mail: String,
    val role: String,
){
    fun toUserView() : UserView {
        return UserView(identifier = identifier, mail = mail, role = role, password= password)
    }
}

data class RemoteUser(
    val identifier: String,
    val mail: String,
    val role: String,
){
    fun toUserView() : UserView {
        return UserView(identifier = identifier, mail = mail, role = role)
    }
}