package com.example.http

enum class HttpMethod {
    GET, POST
}

data class Request(
    val method: HttpMethod = HttpMethod.GET,
    val url: String,
    val headers: Map<String, String> = emptyMap(),
    val body: String? = null
) {
    fun addHeader(name: String, value: String): Request {
        val newHeaders = headers + (name to value)
        return copy(headers = newHeaders)
    }

    fun setBody(body: String): Request {
        return copy(body = body)
    }
}