package com.poc.authserver.utils

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.stereotype.Component
import java.io.IOException

@Component
class CustomAccessDeniedHandler : AccessDeniedHandler {
    @Throws(IOException::class)
    override fun handle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        accessDeniedException: AccessDeniedException
    ) {
        println("🚫 Accès refusé pour ${request.requestURI} - Retour 403 Forbidden")

        response.status = HttpServletResponse.SC_FORBIDDEN
        response.contentType = "application/json"
        response.characterEncoding = "UTF-8"
        response.writer.write("{\"message\": \"Vous n'avez pas les droits nécessaires\"}")
    }
}
