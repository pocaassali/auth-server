package com.poc.authserver.utils

import com.poc.authserver.core.application.ports.output.Users
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import java.util.*

@Service
class CustomUserDetailsService(
    private val users: Users
): UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        val user = users.findById(UUID.fromString(username))
            ?: throw UsernameNotFoundException("User not found: $username")

        return User.withUsername(user.identifier.toString())
            .password(user.password.value)
            .roles(user.role.name)
            .build()
    }
}