package com.example.http

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class RequestTest : StringSpec({

    "should create a request with the correct URL and HTTP method" {
        val request = Request(url = "https://api.example.com/data", method = HttpMethod.POST)
        request.url shouldBe "https://api.example.com/data"
        request.method shouldBe HttpMethod.POST
    }

    "should add a header to the request correctly" {
        val request = Request(url = "https://api.example.com/data")
            .addHeader("Content-Type", "application/json")
        request.headers["Content-Type"] shouldBe "application/json"
    }

    "should set the request body content successfully" {
        val request = Request(url = "https://api.example.com/data")
            .setBody("Test body")
        request.body shouldBe "Test body"
    }

    "should override existing headers when adding a header with the same name" {
        val request = Request(url = "https://api.example.com/data")
            .addHeader("Content-Type", "application/json")
            .addHeader("Content-Type", "text/plain")
        request.headers["Content-Type"] shouldBe "text/plain"
    }
})