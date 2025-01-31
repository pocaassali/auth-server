package com.poc.authserver.filter

import com.poc.authserver.utils.JwtUtil
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtFilter(private val jwtUtil: JwtUtil, private val userDetailsService: UserDetailsService) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authHeader = request.getHeader("Authorization")

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            println("🔴 Aucun token trouvé ou format incorrect")
            filterChain.doFilter(request, response)
            return
        }

        val token = authHeader.substring(7)
        val username = jwtUtil.extractUsername(token)
        println("🟡 JWT détecté pour : $username")

        if (username != null && SecurityContextHolder.getContext().authentication == null) {
            val userDetails = userDetailsService.loadUserByUsername(username)

            if (jwtUtil.validateToken(token, userDetails)) {
                val authToken = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
                authToken.details = WebAuthenticationDetailsSource().buildDetails(request)
                SecurityContextHolder.getContext().authentication = authToken
                println("✅ Authentification réussie pour : ${userDetails.username}")
            } else {
                println("🔴 Token invalide pour $username")
            }
        }

        if (SecurityContextHolder.getContext().authentication != null) {
            println("✅ Utilisateur authentifié : ${SecurityContextHolder.getContext().authentication.name}")
            println("🟡 Autorités actuelles : ${SecurityContextHolder.getContext().authentication.authorities}")
        } else {
            println("🔴 L'utilisateur n'est pas authentifié dans le SecurityContext")
        }

        filterChain.doFilter(request, response)
    }
}
