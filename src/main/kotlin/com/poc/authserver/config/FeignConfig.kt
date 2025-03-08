package com.poc.authserver.config

import com.poc.authserver.infrastructure.api.remote.ServiceUsersFeign
import com.poc.authserver.utils.CustomErrorDecoder
import feign.Contract
import feign.Feign
import feign.Request
import feign.codec.Decoder
import feign.codec.Encoder
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.cloud.openfeign.FeignClientsConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import java.util.concurrent.TimeUnit

@Configuration
@Import(FeignClientsConfiguration::class)
class FeignConfig(
    private val encoder: Encoder,
    private val decoder: Decoder,
    private val contract: Contract,
    @Value("\${remotes.service-users.url}")
    private val serviceUsersUrl: String
) {
    @Bean
    fun okHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .followRedirects(true)
            .connectionPool(ConnectionPool(5, 5, TimeUnit.MINUTES)) // Pool de connexions
            .build()
    }

    @Bean
    fun feign(): Feign.Builder {
        return Feign.builder()
            .client(feign.okhttp.OkHttpClient(okHttpClient()))
            .encoder(encoder)
            .decoder(decoder)
            .contract(contract)
            .errorDecoder(CustomErrorDecoder())
            .options(Request.Options(10, TimeUnit.SECONDS, 30, TimeUnit.SECONDS, true))
    }


    @Bean
    fun serviceUsersFeign(): ServiceUsersFeign = feign().target(ServiceUsersFeign::class.java, serviceUsersUrl)


}