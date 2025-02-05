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
        /*val authHeader = request.getHeader(HEADER_AUTHORIZATION)

        if (authHeader == null || !authHeader.startsWith(HEADER_AUTHORIZATION_PREFIX)) {
            println("🔴 Aucun header trouvé ou format incorrect")
            filterChain.doFilter(request, response)
            return
        }*/

        val sessionId = request.cookies?.firstOrNull { it.name == "SESSION_ID" }?.value

        println(sessionId)

        if (sessionId != null) {
            val session = jwtSessionService.getSession(sessionId)
            if (session != null && !jwtUtil.isTokenExpired(session.accessToken)) {
                println(session.accessToken)
                val username = jwtUtil.extractUsername(session.accessToken)
                val customUserDetails = customUserDetailsService.loadUserByUsername(username)

                if (session.accessToken.isNotBlank() && customUserDetails.let { jwtUtil.isTokenValid(session.accessToken, it) }) {

                    val authToken = UsernamePasswordAuthenticationToken(
                        customUserDetails, null, customUserDetails.authorities
                    )
                    SecurityContextHolder.getContext().authentication = authToken
                }
            }

        }
        /*if (userId.isNotBlank()) {
            val jwt = jwtSessionService.getAccessToken(userId)
            println(jwt)
            val username = jwt?.let { jwtUtil.extractUsername(it) }
            val customUserDetails = username?.let { customUserDetailsService.loadUserByUsername(userId) }

            if (!jwt.isNullOrBlank() && customUserDetails?.let { jwtUtil.isTokenValid(jwt, it) } == true) {

                val authToken = UsernamePasswordAuthenticationToken(
                    customUserDetails, null, customUserDetails.authorities
                )
                SecurityContextHolder.getContext().authentication = authToken
            }
        }

        filterChain.doFilter(request, response)*/
        filterChain.doFilter(request, response)
    }

}