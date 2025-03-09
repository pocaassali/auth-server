package com.poc.authserver.utils

import com.poc.authserver.controller.remote.UserView
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class CustomUserDetails(
    val userId: String,
    private val username: String,
    private val authorities: Collection<GrantedAuthority>
) : UserDetails {

    override fun getAuthorities(): Collection<GrantedAuthority> = authorities

    override fun getPassword(): String = ""

    override fun getUsername(): String = username

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = true

    companion object {
        fun fromUserView(userView: UserView): CustomUserDetails {
            return CustomUserDetails(
                userId = userView.identifier,
                username = userView.identifier,
                authorities = listOf(SimpleGrantedAuthority("ROLE_${userView.role}"))
            )
        }
    }
}