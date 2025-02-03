package com.poc.authserver.infrastructure.api.auth

data class TokensResponse(val accessToken: String, val refreshToken: String)
data class LoginResponse(val sessionId: String)