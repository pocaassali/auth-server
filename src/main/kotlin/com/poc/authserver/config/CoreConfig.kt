package com.poc.authserver.config

import com.poc.authserver.core.application.ports.output.Tokens
import com.poc.authserver.core.application.ports.output.Users
import com.poc.authserver.core.application.service.*
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
        getRefreshTokenByToken: GetRefreshTokenByToken,
        deleteRefreshToken: DeleteRefreshToken,
        saveRefreshToken: SaveRefreshToken,
    ) = AuthApplicationServiceImpl(
        getUserByCredentials = getUserByCredentials,
        getRefreshTokenByToken = getRefreshTokenByToken,
        deleteRefreshToken = deleteRefreshToken,
        saveRefreshToken = saveRefreshToken
    )

    @Bean
    fun getUserByCredentials(users: Users) = GetUserByCredentials(users)

    @Bean
    fun getToken(tokens: Tokens) = GetRefreshTokenByToken(tokens)

    @Bean
    fun deleteRefreshToken(tokens: Tokens) = DeleteRefreshToken(tokens)

    @Bean
    fun saveRefreshToken(tokens: Tokens) = SaveRefreshToken(tokens)
}