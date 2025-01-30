package com.poc.authserver.users.core.application.ports.input

import com.poc.authserver.users.core.application.dto.query.GetUserByCredentialsQuery
import com.poc.authserver.users.core.domain.model.User

interface AuthApplicationService {
    fun getUserByCredentials(query: GetUserByCredentialsQuery) : User?
}