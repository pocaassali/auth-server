package com.poc.authserver.config

import com.poc.authserver.config.SecurityPermissions.PUBLIC_ENDPOINTS
import com.poc.authserver.filter.JwtSessionFilter
import com.poc.authserver.utils.CustomAccessDeniedHandler
import com.poc.authserver.utils.CustomAuthenticationEntryPoint
import com.poc.authserver.utils.CustomUserDetailsService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter


@Configuration
@EnableMethodSecurity(prePostEnabled = true)
class SecurityConfig(
    private val jwtSessionFilter: JwtSessionFilter,
    private val customUserDetailsService : CustomUserDetailsService
) {

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .logout { it.disable() }
            .csrf { it.disable() }
            .sessionManagement {it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)}
            .authorizeHttpRequests {
                it.requestMatchers(*PUBLIC_ENDPOINTS.toTypedArray())
                    .permitAll()
                    .anyRequest()
                    .authenticated()
            }
            .exceptionHandling {
                it.authenticationEntryPoint(CustomAuthenticationEntryPoint())
                it.accessDeniedHandler(CustomAccessDeniedHandler())
            }
            .addFilterBefore(jwtSessionFilter, UsernamePasswordAuthenticationFilter::class.java)
            .formLogin { it.disable() }

        return http.build()
    }

    @Bean
    fun authenticationManager(authenticationConfiguration: AuthenticationConfiguration): AuthenticationManager =
        authenticationConfiguration.authenticationManager

    @Bean
    fun authenticationProvider(): DaoAuthenticationProvider {
        val authProvider = DaoAuthenticationProvider()
        authProvider.setUserDetailsService(customUserDetailsService)
        authProvider.setPasswordEncoder(passwordEncoder())
        return authProvider
    }
}

object SecurityPermissions {
    val PUBLIC_ENDPOINTS = listOf(
        "/login",
        "/refresh",
        "/logout",
        "/signup",
        "/static/**",
        "/css/**",
        "/js/**",
        "/images/**",
        "/favicon.ico"
    )


    val ADMIN_ENDPOINTS = mapOf(
        HttpMethod.PUT to listOf("/users/{id}"),
        //HttpMethod.DELETE to listOf("/users/{id}")
    )
}
