package com.poc.authserver.infrastructure.api.auth

import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class AuthController(
    private val authAdapter: AuthAdapter
) {

    /*@PostMapping("/login")
    fun loginWithCredentials(@RequestBody request: LoginRequest) : ResponseEntity<TokensResponse?> {
        return ResponseEntity.ok(authAdapter.login(request))
    }*/

    /*@PostMapping("/login")
    fun loginWithSession(@RequestBody request: LoginRequest) : ResponseEntity<LoginResponse?> {
        return ResponseEntity.ok(authAdapter.loginSessionBased(request))
    }*/

    @PostMapping("/login")
    fun loginWithSessionCookie(
        /*@RequestParam username: String,
        @RequestParam password: String,*/
        @ModelAttribute request: LoginRequest,
        response: HttpServletResponse
    ): ResponseEntity<String> {
        //val request = LoginRequest(username, password)
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

    /*@PostMapping("/refresh")
    fun refreshToken(@RequestBody request: RefreshTokenRequest): ResponseEntity<TokensResponse?> {
        return ResponseEntity.ok(authAdapter.refreshToken(request)) //if adapter return null return 401 here
    }*/

    /*@PostMapping("/refresh")
    fun refreshToken(@RequestHeader("USER_ID") userId: String): ResponseEntity<Unit> {
        authAdapter.refreshTokenSessionBased(userId)
        return ResponseEntity.ok().build()
    }*/

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

    /*@PostMapping("/logout")
    fun logout(@RequestBody request : LogoutRequest) : ResponseEntity<String> {
        authAdapter.logout(request)
        return ResponseEntity.ok("User with id ${request.userId} successfully logged out")
    }*/

    /*@PostMapping("/logout")
    fun logout(@RequestHeader("USER_ID") userId : String) : ResponseEntity<String> {
        authAdapter.logoutSessionBased(userId)
        return ResponseEntity.ok("Session with id $userId successfully revoked")
    }*/

    @PostMapping("/logout")
    fun logout(
        @CookieValue(name = "SESSION_ID", required = false) sessionId: String?,
        response: HttpServletResponse,
    ): ResponseEntity<String> {
        println("sesion to delete: $sessionId")
        if (sessionId != null) {
            authAdapter.logoutSessionBased(sessionId)
        }

        // Supprime le cookie en le remplaçant par un cookie vide avec maxAge = 0
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