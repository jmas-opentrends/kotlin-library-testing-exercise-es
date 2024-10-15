package com.example.http

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec

import io.kotest.matchers.shouldBe
import io.mockk.*
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import java.io.ByteArrayInputStream
import java.net.HttpURLConnection

class HttpClientTest : DescribeSpec({
    describe("execute") {
        it("execute should return response on successful request") {
            // Arrange
            val mockUrlConnection = mockk<HttpURLConnection>(relaxed = true)
            val mockInputStream = ByteArrayInputStream("Success".toByteArray())

            every { mockUrlConnection.responseCode } returns 200
            every { mockUrlConnection.headerFields } returns mapOf("Content-Type" to listOf("application/json"))
            every { mockUrlConnection.inputStream } returns mockInputStream

            val connectionFactory: (String) -> HttpURLConnection = { mockUrlConnection }

            val request = Request(
                url = "http://example.com",
                method = HttpMethod.GET
            )

            val client = HttpClient(connectionFactory)

            // Act
            val response = client.execute(request)

            // Assert
            response.statusCode shouldBe 200
            response.headers["Content-Type"] shouldBe "application/json"
            response.body shouldBe "Success"
        }

        it("execute should throw HttpException on unsuccessful request") {
            // Arrange
            val mockUrlConnection = mockk<HttpURLConnection>(relaxed = true)
            val mockErrorStream = ByteArrayInputStream("Error".toByteArray())

            every { mockUrlConnection.responseCode } returns 404
            every { mockUrlConnection.headerFields } returns mapOf("Content-Type" to listOf("application/json"))
            every { mockUrlConnection.errorStream } returns mockErrorStream


            val request = Request(
                url = "http://example.com/notfound",
                method = HttpMethod.GET
            )

            val connectionFactory: (String) -> HttpURLConnection = { mockUrlConnection }
            val client = HttpClient(connectionFactory)

            // Act & Assert
            val exception = shouldThrow<HttpException> {
                client.execute(request)
            }

            exception.message shouldBe "Request failed with status code 404"
            exception.statusCode shouldBe 404
        }

        it("executeAsync should return response on successful request") {
            // Arrange
            val mockUrlConnection = mockk<HttpURLConnection>(relaxed = true)
            val mockInputStream = ByteArrayInputStream("Async Success".toByteArray())

            every { mockUrlConnection.responseCode } returns 200
            every { mockUrlConnection.headerFields } returns mapOf("Content-Type" to listOf("application/json"))
            every { mockUrlConnection.inputStream } returns mockInputStream


            val request = Request(
                url = "http://example.com",
                method = HttpMethod.GET
            )

            val connectionFactory: (String) -> HttpURLConnection = { mockUrlConnection }
            val client = HttpClient(connectionFactory)

            // Act
            val response = runBlocking {
                client.executeAsync(request)
            }

            // Assert
            response.statusCode shouldBe 200
            response.headers["Content-Type"] shouldBe "application/json"
            response.body shouldBe "Async Success"
        }
    }

    describe("executeAsync") {

        it("executeAsync should throw HttpException on unsuccessful request") {
            // Arrange
            val mockUrlConnection = mockk<HttpURLConnection>(relaxed = true)
            val mockErrorStream = ByteArrayInputStream("Async Error".toByteArray())

            every { mockUrlConnection.responseCode } returns 500
            every { mockUrlConnection.headerFields } returns mapOf("Content-Type" to listOf("application/json"))
            every { mockUrlConnection.errorStream } returns mockErrorStream


            val request = Request(
                url = "http://example.com/error",
                method = HttpMethod.GET
            )

            // Act & Assert
            val exception = shouldThrow<HttpException> {
                runTest {
                    val connectionFactory: (String) -> HttpURLConnection = { mockUrlConnection }
                    val dispatcher = StandardTestDispatcher(testScheduler)
                    val client = HttpClient(connectionFactory, dispatcher)
                    client.executeAsync(request)
                }
            }

            exception.message shouldBe "Request failed with status code 500"
            exception.statusCode shouldBe 500
        }
    }
})