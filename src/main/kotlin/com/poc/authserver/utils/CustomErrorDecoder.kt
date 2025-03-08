package com.poc.authserver.utils

import com.fasterxml.jackson.databind.ObjectMapper
import feign.Response
import feign.codec.ErrorDecoder
import feign.codec.ErrorDecoder.Default
import org.springframework.http.HttpStatus

const val DEFAULT_ERROR_MESSAGE = "An error occurred !"

class CustomErrorDecoder : ErrorDecoder {

    private val objectMapper = ObjectMapper()

    override fun decode(methodKey: String, response: Response): Exception {
        return when (response.status()) {
            HttpStatus.BAD_REQUEST.value() -> {
                val errorMessage = extractErrorMessage(response)
                IllegalArgumentException(errorMessage)
            }
            else -> {
                Default().decode(methodKey, response)
            }
        }
    }

    private fun extractErrorMessage(response: Response): String {
        val responseBody = response.body().asInputStream().use { it.readAllBytes().decodeToString() }
        val jsonNode = objectMapper.readTree(responseBody)
        return jsonNode.get(DEFAULT_ERROR_MESSAGE).asText()

    }
}