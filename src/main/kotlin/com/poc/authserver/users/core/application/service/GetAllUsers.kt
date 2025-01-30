package com.poc.authserver.users.core.application.service

import com.poc.authserver.users.core.application.dto.query.GetAllUsersQuery
import com.poc.authserver.users.core.application.ports.output.Users
import com.poc.authserver.users.core.domain.model.User

class GetAllUsers(
    private val users: Users
) : AbstractQueryHandler<GetAllUsersQuery,List<User>>() {
    override fun execute(query: GetAllUsersQuery) : List<User> {
        return users.findAll()
    }
}
