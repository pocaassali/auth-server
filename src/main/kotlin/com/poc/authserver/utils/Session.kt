package com.poc.authserver.utils

data class Session(
    val id: String,
    val data: Map<String, String>
)