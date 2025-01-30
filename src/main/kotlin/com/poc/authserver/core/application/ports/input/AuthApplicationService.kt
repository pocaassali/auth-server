package com.poc.authserver.core.application.ports.input

import com.poc.authserver.core.application.dto.query.GetUserByCredentialsQuery
import com.poc.authserver.core.domain.model.User

interface AuthApplicationService {
    fun getUserByCredentials(query: GetUserByCredentialsQuery) : User?
}