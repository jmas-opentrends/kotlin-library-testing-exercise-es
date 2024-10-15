package com.example.http

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class ResponseTest : StringSpec({

    "should parse and return the response status code correctly" {
        val response = Response(statusCode = 200, headers = emptyMap(), body = null)
        response.statusCode shouldBe 200
    }

    "should store and retrieve headers from the response" {
        val headers = mapOf("Content-Type" to "application/json")
        val response = Response(statusCode = 200, headers = headers, body = null)
        response.headers["Content-Type"] shouldBe "application/json"
    }

    "should return the response body as a string" {
        val response = Response(statusCode = 200, headers = emptyMap(), body = "Response body")
        response.body shouldBe "Response body"
    }

    "should handle empty response body gracefully" {
        val response = Response(statusCode = 200, headers = emptyMap(), body = null)
        response.body shouldBe null
    }
})