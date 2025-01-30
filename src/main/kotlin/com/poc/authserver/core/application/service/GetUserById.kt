package com.poc.authserver.core.application.service

import com.poc.authserver.core.application.dto.query.GetUserByIdQuery
import com.poc.authserver.core.application.ports.output.Users
import com.poc.authserver.core.domain.model.User

class GetUserById(
    private val users: Users
) : AbstractQueryHandler<GetUserByIdQuery, User?>() {
    override fun execute(query: GetUserByIdQuery) : User? {
        return users.findById(query.id)
    }
}
