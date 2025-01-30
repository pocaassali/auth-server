package com.poc.authserver.core.application.ports.output

import com.poc.authserver.core.domain.model.User
import com.poc.authserver.core.domain.valueobject.Credentials
import java.util.*

interface Users {
    fun save(user: User): User?
    fun findAll(): List<User>
    fun findById(id: UUID): User?
    fun update(user: User): User?
    fun delete(id: UUID)
    fun findByCredentials(credentials : Credentials): User?
}