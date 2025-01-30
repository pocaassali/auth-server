package com.poc.authserver.users.config

import com.poc.authserver.users.core.application.ports.output.Users
import com.poc.authserver.users.core.application.service.CreateUser
import com.poc.authserver.users.core.application.service.GetAllUsers
import com.poc.authserver.users.core.application.service.GetUserById
import com.poc.authserver.users.core.application.service.UserApplicationServiceImpl
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CoreConfig {

    @Bean
    fun userApplicationConfig(
        createUser : CreateUser,
        getAllUsers: GetAllUsers,
        getUserById: GetUserById,
    ) = UserApplicationServiceImpl(createUser = createUser, getAllUsers = getAllUsers, getUserById = getUserById)

    @Bean
    fun createUser(users: Users) = CreateUser(users = users)

    @Bean
    fun getAllUsers(users: Users) = GetAllUsers(users = users)

    @Bean
    fun getUserById(users: Users) = GetUserById(users = users)
}