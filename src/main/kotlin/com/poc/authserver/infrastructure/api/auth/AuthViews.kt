package com.poc.authserver.infrastructure.api.auth

data class TokensResponse(val accessToken: String, val refreshToken: String)