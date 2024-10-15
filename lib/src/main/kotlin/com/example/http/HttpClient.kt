package com.example.http

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

fun defaultUrlConnectionFactory(url: String) = URL(url).openConnection() as HttpURLConnection

class HttpClient(
    private val urlConnectionFactory: (String) -> HttpURLConnection = ::defaultUrlConnectionFactory,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO) {
    fun execute(request: Request): Response {
        return try {
            val urlConnection = prepareConnection(request)
            val statusCode = urlConnection.responseCode
            val headers = urlConnection.headerFields.mapValues { it.value.first() }

            val inputStream = if(statusCode in 200..299) {
                urlConnection.inputStream
            } else {
                urlConnection.errorStream
            }

            val body = inputStream?.bufferedReader()?.use(BufferedReader::readText)

            if(statusCode !in 200..299) {
                throw HttpException("Request failed with status code $statusCode", statusCode)
            }

            Response(statusCode, headers, body)
        } catch (e: Exception) {
            throw if (e is HttpException) e else HttpException("Request failed", null)
        }
    }

    suspend fun executeAsync(request: Request) =  withContext(dispatcher) {
        execute(request)
    }

    private fun prepareConnection(request: Request): HttpURLConnection {
        val urlConnection = urlConnectionFactory(request.url)
        urlConnection.requestMethod = request.method.name
        request.headers.forEach { (key, value) ->
            urlConnection.setRequestProperty(key, value)
        }
        urlConnection.connectTimeout = 10000
        urlConnection.readTimeout = 10000
        urlConnection.doOutput = true
        return urlConnection
    }
}

class HttpException(message: String, val statusCode: Int? = null) : Exception(message)