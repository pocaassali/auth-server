package com.poc.authserver.filter

import com.poc.authserver.config.SecurityPermissions
import com.poc.authserver.utils.CustomUserDetailsService
import com.poc.authserver.utils.JwtSessionService
import com.poc.authserver.utils.JwtUtil
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.util.AntPathMatcher
import org.springframework.web.filter.OncePerRequestFilter

const val HEADER_AUTHORIZATION = "Authorization"
const val HEADER_AUTHORIZATION_PREFIX = "User "

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

        val sessionId = request.cookies?.firstOrNull { it.name == "SESSION_ID" }?.value

        if (sessionId != null) {
            val session = jwtSessionService.getSession("sessionId")
            if (session != null && !jwtUtil.isTokenExpired(session.accessToken)) {
                println(session.accessToken)
                val username = jwtUtil.extractUsername(session.accessToken)
                val customUserDetails = customUserDetailsService.loadUserByUsername(username)

                if (session.accessToken.isNotBlank() && customUserDetails.let {
                        jwtUtil.isTokenValid(
                            session.accessToken,
                            it
                        )
                    }) {

                    val authToken = UsernamePasswordAuthenticationToken(
                        customUserDetails, null, customUserDetails.authorities
                    )
                    SecurityContextHolder.getContext().authentication = authToken
                }
            }
        }
        filterChain.doFilter(request, response)
    }

}