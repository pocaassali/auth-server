package com.poc.authserver.users.config

import com.poc.authserver.users.core.application.ports.input.AuthApplicationService
import com.poc.authserver.users.core.application.ports.output.Users
import com.poc.authserver.users.core.application.service.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CoreConfig {

    @Bean
    fun userApplicationConfig(
        createUser: CreateUser,
        getAllUsers: GetAllUsers,
        getUserById: GetUserById,
        updateUser: UpdateUser,
        deleteUser: DeleteUser,
    ) = UserApplicationServiceImpl(
        createUser = createUser,
        getAllUsers = getAllUsers,
        getUserById = getUserById,
        updateUser = updateUser,
        deleteUser = deleteUser
    )

    @Bean
    fun createUser(users: Users) = CreateUser(users = users)

    @Bean
    fun getAllUsers(users: Users) = GetAllUsers(users = users)

    @Bean
    fun getUserById(users: Users) = GetUserById(users = users)

    @Bean
    fun updateUser(users: Users) = UpdateUser(users = users)

    @Bean
    fun deleteUser(users: Users) = DeleteUser(users = users)

    @Bean
    fun authApplicationService(
        getUserByCredentials: GetUserByCredentials,
    ) = AuthApplicationServiceImpl(
        getUserByCredentials = getUserByCredentials
    )

    @Bean
    fun getUserByCredentials(users: Users) = GetUserByCredentials(users)
}