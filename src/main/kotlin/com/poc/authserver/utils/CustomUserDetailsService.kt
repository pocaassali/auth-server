package com.poc.authserver.utils

import com.poc.authserver.controller.remote.ServiceUsersFeign
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import java.util.*

@Service
class CustomUserDetailsService(
    private val serviceUsersFeign: ServiceUsersFeign
): UserDetailsService {

    override fun loadUserByUsername(username: String): CustomUserDetails {
        val user = serviceUsersFeign.getUserByIdentifier(username)?.toUserView()
            ?: throw UsernameNotFoundException("User not found: $username")

        return CustomUserDetails(
            userId = user.identifier,
            username = user.identifier,
            authorities = listOf(SimpleGrantedAuthority("ROLE_${user.role}"))
        )
    }
}