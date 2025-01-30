package com.poc.authserver.users.core.application.service

import com.poc.authserver.users.core.application.dto.query.GetUserByCredentialsQuery
import com.poc.authserver.users.core.application.ports.output.Users
import com.poc.authserver.users.core.domain.model.User

class GetUserByCredentials(
    private val users: Users
) : AbstractQueryHandler<GetUserByCredentialsQuery,User?>() {
    override fun execute(query: GetUserByCredentialsQuery) : User? {
        return users.findByCredentials(query.toCredentials())
    }
}
