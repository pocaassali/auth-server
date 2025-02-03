package com.poc.authserver.filter

import com.poc.authserver.utils.CustomUserDetailsService
import com.poc.authserver.utils.JwtSessionService
import com.poc.authserver.utils.JwtUtil
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtSessionFilter(
    private val jwtUtil: JwtUtil,
    private val jwtSessionService: JwtSessionService,
    private val customUserDetailsService: CustomUserDetailsService
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val sessionId = request.getHeader("SESSION_ID")

        if (!sessionId.isNullOrBlank()) {
            val jwt = jwtSessionService.getJwt(sessionId)
            val username = jwt?.let { jwtUtil.extractUsername(it) }
            val customUserDetails = username?.let { customUserDetailsService.loadUserByUsername(it) }

            if (!jwt.isNullOrBlank() && customUserDetails?.let { jwtUtil.isTokenValid(jwt, it) } == true) {

                val userDetails = customUserDetailsService.loadUserByUsername(username)

                val authToken = UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.authorities
                )
                SecurityContextHolder.getContext().authentication = authToken
            }
        }

        filterChain.doFilter(request, response)
    }

}