package com.poc.authserver.users.core.application.service

import com.poc.authserver.users.core.application.dto.query.GetAllUsersQuery
import com.poc.authserver.users.core.application.dto.query.GetUserByIdQuery
import com.poc.authserver.users.core.application.ports.output.Users
import com.poc.authserver.users.core.domain.model.User

class GetUserById(
    private val users: Users
) : AbstractQueryHandler<GetUserByIdQuery,User?>() {
    override fun execute(query: GetUserByIdQuery) : User? {
        return users.findById(query.id)
    }
}
