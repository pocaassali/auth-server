package com.poc.authserver.controller.remote

private const val HIDE_PASSWORD = "********"

data class UserView(
    val identifier : String,
    val password : String = HIDE_PASSWORD,
    val mail : String,
    val role : String,
)