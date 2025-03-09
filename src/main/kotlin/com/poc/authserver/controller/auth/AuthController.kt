package com.poc.authserver.controller.auth

import com.poc.authserver.controller.remote.RemoteUserRegisterRequest
import com.poc.authserver.controller.remote.ServiceUsersFeign
import com.poc.authserver.controller.remote.UserView
import com.poc.authserver.utils.CustomUserDetails
import com.poc.authserver.utils.JwtSessionService
import com.poc.authserver.utils.JwtUtil
import com.poc.authserver.utils.Session
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.CookieValue
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

private const val SESSION_COOKIE_NAME = "SESSION_ID"
typealias ApiResponse = Map<String, Any>

@RestController
class AuthController(
    private val passwordEncoder: PasswordEncoder,
    private val jwtUtil: JwtUtil,
    private val jwtSessionService: JwtSessionService,
    private val serviceUsersFeign: ServiceUsersFeign,
) {

    @PostMapping("/login")
    fun login(
        @ModelAttribute request: LoginRequest,
        @CookieValue(name = "SESSION_ID", required = false) cookieSessionId: String?,
        response: HttpServletResponse
    ): ResponseEntity<ApiResponse> {
        val sessionId = handleLogin(request, cookieSessionId)?.sessionId
        return if (sessionId != null) {
            response.addCookie(createSessionCookie(sessionId))
            ResponseEntity.ok(mapOf("message" to "Login successful"))
        } else {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(mapOf("message" to "Invalid credentials"))
        }
    }

    @PostMapping("/refresh")
    fun refreshToken(
        @CookieValue(name = "SESSION_ID", required = false) sessionId: String?
    ): ResponseEntity<ApiResponse> {
        if (sessionId == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(mapOf("message" to "Session not found"))
        }
        handleTokensRefresh(sessionId)

        return ResponseEntity.ok(mapOf("message" to "Access token refreshed"))
    }

    @PostMapping("/logout")
    fun logout(
        @CookieValue(name = SESSION_COOKIE_NAME, required = false) cookieSessionId: String?,
        response: HttpServletResponse,
    ): ResponseEntity<ApiResponse> {
        return cookieSessionId?.let {
            handleLogout(it)
            val expiredCookie = createExpiredSessionCookie()
            response.addCookie(expiredCookie)
            ResponseEntity.ok(mapOf("message" to "Session with id $cookieSessionId successfully deleted"))
        } ?: run {
            ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(mapOf("message" to "No active session found"))
        }


    }

    @PostMapping("/register")
    fun register(@ModelAttribute request: RegisterRequest): ResponseEntity<UserView> {
        return ResponseEntity.ok(handleRegister(request))
    }

    private fun handleLogin(request: LoginRequest, previousSessionId: String?): LoginResponse? {
        val user = serviceUsersFeign.getUserByCredentials(request.toRemoteRequest())?.toUserView() ?: return null
        if (isNotMatch(request.password, user.password)) return null

        previousSessionId?.let { handleLogout(it) }
        val userDetails = CustomUserDetails.fromUserView(user)
        val newSession = createSession(userDetails = userDetails)

        return LoginResponse(newSession.id)
    }

    private fun handleTokensRefresh(sessionId: String) {
        val storedSession = jwtSessionService.getSession(sessionId) ?: return
        val refreshToken = storedSession.data["refreshToken"] ?: return

        if (jwtUtil.isTokenExpired(refreshToken)) {
            jwtSessionService.removeSession(sessionId)
            return
        }
        val identifier = jwtUtil.extractUsername(refreshToken)
        val user = serviceUsersFeign
            .getUserByIdentifier(identifier)
            ?.toUserView() ?: return

        val userDetails = CustomUserDetails.fromUserView(user)
        createSession(sessionId = sessionId, userDetails = userDetails)
    }

    private fun handleLogout(sessionId: String) = jwtSessionService.removeSession(sessionId)

    private fun handleRegister(request: RegisterRequest): UserView? {
        val remoteUserRegisterRequest = RemoteUserRegisterRequest(request.mail, request.password)
        return try {
            serviceUsersFeign
                .registerUser(remoteUserRegisterRequest)
                ?.toUserView()
        } catch (e: Exception) {
            println(e.message)
            null
        }
    }

    private fun isNotMatch(rawPassword: String, encodedPassword: String): Boolean {
        return !passwordEncoder.matches(rawPassword, encodedPassword)
    }


    private fun createSessionCookie(sessionId: String): Cookie {
        val cookieMaxAgeInSeconds = 7 * 24 * 60 * 60 // 7 days
        return Cookie(SESSION_COOKIE_NAME, sessionId).apply {
            isHttpOnly = true
            secure = true
            path = "/"
            maxAge = cookieMaxAgeInSeconds
        }
    }

    private fun createExpiredSessionCookie(): Cookie {
        return Cookie(SESSION_COOKIE_NAME, "").apply {
            isHttpOnly = true
            secure = true
            path = "/"
            maxAge = 0
        }
    }

    private fun createSession(sessionId: String? = null, userDetails: CustomUserDetails): Session {
        val accessToken = jwtUtil.generateToken(userDetails)
        val refreshToken = jwtUtil.generateRefreshToken(userDetails)
        val newSessionId = sessionId ?: UUID.randomUUID().toString()
        val session = Session(newSessionId, mapOf("accessToken" to accessToken, "refreshToken" to refreshToken))
        return jwtSessionService.storeSession(session)
    }

}

