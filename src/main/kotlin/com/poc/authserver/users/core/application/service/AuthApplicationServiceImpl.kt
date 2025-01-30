package com.poc.authserver.users.core.application.service

import com.poc.authserver.users.core.application.dto.query.GetUserByCredentialsQuery
import com.poc.authserver.users.core.application.ports.input.AuthApplicationService
import com.poc.authserver.users.core.domain.model.User

class AuthApplicationServiceImpl(
    private val getUserByCredentials: GetUserByCredentials,
) : AuthApplicationService {

    override fun getUserByCredentials(query: GetUserByCredentialsQuery) : User? {
        return getUserByCredentials.handle(query)
    }

}