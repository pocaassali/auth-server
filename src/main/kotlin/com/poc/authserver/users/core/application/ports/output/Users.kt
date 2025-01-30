package com.poc.authserver.users.core.application.ports.output

import com.poc.authserver.users.core.domain.model.User

interface Users {
    fun save(user: User): User
    fun findAll(): List<User>
}