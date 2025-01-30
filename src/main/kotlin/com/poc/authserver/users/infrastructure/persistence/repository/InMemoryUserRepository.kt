package com.poc.authserver.users.infrastructure.persistence.repository

import com.poc.authserver.users.core.application.ports.output.Users
import com.poc.authserver.users.infrastructure.persistence.entity.UserEntity
import org.springframework.stereotype.Repository

@Repository
class InMemoryUserRepository : Users {

    private val users = mutableMapOf(
        Pair(1L,UserEntity(identifier = "", mail = "", password = "", role = "")),
        Pair(1L,UserEntity(identifier = "", mail = "", password = "", role = ""))
    )
}