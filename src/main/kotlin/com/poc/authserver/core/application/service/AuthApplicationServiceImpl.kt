package com.poc.authserver.core.application.service

import com.poc.authserver.core.application.dto.query.GetUserByCredentialsQuery
import com.poc.authserver.core.application.ports.input.AuthApplicationService
import com.poc.authserver.core.domain.model.User

class AuthApplicationServiceImpl(
    private val getUserByCredentials: GetUserByCredentials,
) : AuthApplicationService {

    override fun getUserByCredentials(query: GetUserByCredentialsQuery) : User? {
        return getUserByCredentials.handle(query)
    }

}