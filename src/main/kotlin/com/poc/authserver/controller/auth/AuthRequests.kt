package com.poc.authserver.controller.auth

import com.poc.authserver.controller.remote.RemoteUserLoginRequest


data class LoginRequest(val username: String, val password: String){
    fun toRemoteRequest() : RemoteUserLoginRequest {
        return RemoteUserLoginRequest(username)
    }
}

data class RegisterRequest(
    val identifier : String = "3d41a343-3c56-4462-9f80-97b5eabb82fa",
    val mail : String,
    val password : String,
    val role : String = "USER",
)