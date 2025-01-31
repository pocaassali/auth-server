package com.poc.authserver.utils

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

@Component
class AuthServerSecurityGuard {
    fun isSelfOrAdmin(userId: String): Boolean {
        val authentication = SecurityContextHolder.getContext().authentication
        val principal = authentication.principal

        //TODO : adapte with customUserDetails
        if (principal is CustomUserDetails) {
            return principal.userId == userId || authentication.authorities.any { it.authority == "ROLE_ADMIN" }
        }

        return false
    }

    fun hasRole(role: String): Boolean {
        val authentication = SecurityContextHolder.getContext().authentication
        return authentication.authorities.any { it.authority == "ROLE_$role" }
    }

    fun hasAdminRole(): Boolean {
        val authentication = SecurityContextHolder.getContext().authentication
        return authentication.authorities.any { it.authority == "ROLE_ADMIN" }
    }
}