package com.poc.authserver.users.config

import com.poc.authserver.users.core.application.service.UserApplicationServiceImpl
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CoreConfig {

    @Bean
    fun userApplicationConfig() = UserApplicationServiceImpl()
}