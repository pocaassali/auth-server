package com.poc.authserver.infrastructure.persistence.repository

import com.poc.authserver.core.application.ports.output.Users
import com.poc.authserver.core.domain.model.User
import com.poc.authserver.core.domain.valueobject.Credentials
import com.poc.authserver.infrastructure.persistence.entity.UserEntity
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class InMemoryUserRepository : Users {

    private val users = mutableMapOf(
        Pair(1L,
            UserEntity(identifier = "bd657168-e573-4925-900a-d5f26e82760b", mail = "alice@mail.fr", password = "654321", role = "ADMIN")
        ),
        Pair(2L,UserEntity(identifier = "40c273b3-c4fc-4227-9523-e4782a7f2c20", mail = "bob@mail.com", password = "123456", role = "USER"))
    )

    override fun save(user: User): User? {
        val id = (users.size+1).toLong()
        users[id] = UserEntity.from(user)
        return users[id]?.toUser()
    }

    override fun findAll(): List<User> {
        return users.map { it.value.toUser() }
    }

    override fun findById(id: UUID): User? {
        return users.values.find { it.identifier == id.toString() }?.toUser()
    }

    override fun update(user: User): User? {
        val userToUpdate = users.entries.find { UUID.fromString(it.value.identifier) == user.identifier }
        userToUpdate?.let { users[it.key] = UserEntity.from(user) }
        return users[userToUpdate?.key]?.toUser()
    }

    override fun delete(id: UUID) {
        val userToDelete = users.entries.find { UUID.fromString(it.value.identifier) == id }
        userToDelete?.let { users.remove(it.key) }
    }

    override fun findByCredentials(credentials: Credentials): User? {
        val foundUser = users.entries.find { it.value.mail == credentials.mail }
        if (foundUser != null) {
            if(foundUser.value.password == credentials.password) {
                return foundUser.value.toUser()
            }
        }
        return null
    }
}