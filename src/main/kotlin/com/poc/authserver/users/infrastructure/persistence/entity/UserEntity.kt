package com.poc.authserver.users.infrastructure.persistence.entity

data class UserEntity(
    val identifier : String,
    val mail : String,
    val password : String,
    val role : String,
)
