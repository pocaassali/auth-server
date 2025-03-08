package com.poc.authserver.infrastructure.api.remote

import com.poc.authserver.infrastructure.api.user.UserView
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

interface ServiceUsersFeign {
    @PostMapping("/users")
    fun registerUser(@RequestBody request : RemoteRequest) : RemoteResponse?
}

data class RemoteRequest(
    val mail: String,
    val password: String,
    val role: String = "USER"
)

data class RemoteResponse(
    val identifier: String,
    val mail: String,
    val role: String,
){
    fun toUserView() : UserView {
        return UserView(identifier, mail, role)
    }
}