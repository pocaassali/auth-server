package com.poc.authserver.infrastructure.api.auth

import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class AuthController(
    private val authAdapter: AuthAdapter
) {

    @PostMapping("/login")
    fun loginWithSessionCookie(
        @ModelAttribute request: LoginRequest,
        response: HttpServletResponse
    ): ResponseEntity<String> {
        println(request)
        val sessionId = authAdapter.loginSessionBased(request)?.sessionId
        if (sessionId != null) {
            val cookie = Cookie("SESSION_ID", sessionId).apply {
                isHttpOnly = true
                secure = true
                path = "/"
                maxAge = 7 * 24 * 60 * 60
            }
            response.addCookie(cookie)
            return ResponseEntity.ok("Session with Id ${cookie.value} was initiated !")
        }

        return ResponseEntity.ok("An error occurred")
    }

    @PostMapping("/refresh")
    fun refreshToken(
        @CookieValue(name = "SESSION_ID", required = false) sessionId: String?
    ): ResponseEntity<Map<String, String>> {
        if (sessionId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(mapOf("error" to "Session not found"))
        }
        authAdapter.refreshTokenSessionBased(sessionId)

        return ResponseEntity.ok(mapOf("message" to "Access token refreshed"))
    }

    @PostMapping("/logout")
    fun logout(
        @CookieValue(name = "SESSION_ID", required = false) sessionId: String?,
        response: HttpServletResponse,
    ): ResponseEntity<String> {
        println("sesion to delete: $sessionId")
        if (sessionId != null) {
            authAdapter.logoutSessionBased(sessionId)
        }

        val cookie = Cookie("SESSION_ID", "").apply {
            isHttpOnly = true
            secure = true
            path = "/"
            maxAge = 0
        }
        response.addCookie(cookie)

        return ResponseEntity.ok("Session with id $sessionId successfully deleted")
    }
}