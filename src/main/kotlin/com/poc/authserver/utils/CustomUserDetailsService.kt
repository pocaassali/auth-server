package com.poc.authserver.utils

import com.poc.authserver.core.application.ports.output.Users
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import java.util.*

@Service
class CustomUserDetailsService(
    private val users: Users
): UserDetailsService {

    override fun loadUserByUsername(username: String): CustomUserDetails {
        val user = users.findById(UUID.fromString(username))
            ?: throw UsernameNotFoundException("User not found: $username")

        return CustomUserDetails(
            userId = user.identifier.toString(),
            username = user.identifier.toString(),
            password = user.password.value,
            authorities = listOf(SimpleGrantedAuthority("ROLE_${user.role.name}"))
        )
    }
}