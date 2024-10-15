package com.example.http

data class Response (
    val statusCode: Int,
    val headers: Map<String, String> = emptyMap(),
    val body: String?
)