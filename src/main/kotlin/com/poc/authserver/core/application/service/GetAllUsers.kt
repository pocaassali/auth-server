package com.poc.authserver.core.application.service

import com.poc.authserver.core.application.dto.query.GetAllUsersQuery
import com.poc.authserver.core.application.ports.output.Users
import com.poc.authserver.core.domain.model.User

class GetAllUsers(
    private val users: Users
) : AbstractQueryHandler<GetAllUsersQuery,List<User>>() {
    override fun execute(query: GetAllUsersQuery) : List<User> {
        return users.findAll()
    }
}
