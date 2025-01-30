package com.poc.authserver.users.core.application.ports.output

import com.poc.authserver.users.core.domain.model.User
import com.poc.authserver.users.core.domain.valueobject.Credentials
import java.util.*

interface Users {
    fun save(user: User): User?
    fun findAll(): List<User>
    fun findById(id: UUID): User?
    fun update(user: User): User?
    fun delete(id: UUID)
    fun findByCredentials(credentials : Credentials): User?
}