package com.poc.authserver.core.application.service

import com.poc.authserver.core.application.dto.query.GetUserByCredentialsQuery
import com.poc.authserver.core.application.ports.output.Users
import com.poc.authserver.core.domain.model.User

class GetUserByCredentials(
    private val users: Users
) : AbstractQueryHandler<GetUserByCredentialsQuery, User?>() {
    override fun execute(query: GetUserByCredentialsQuery) : User? {
        return users.findByCredentials(query.toCredentials())
    }
}
