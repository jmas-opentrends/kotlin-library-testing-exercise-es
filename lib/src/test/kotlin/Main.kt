package com.example.http

import io.kotest.common.runBlocking

fun main() {
    val client = HttpClient()

    val request = Request(
        url = "https://rickandmortyapi.com/api/character/2",
        method = HttpMethod.GET
    )

    try {
        val response = client.execute(request)
        println("Código de Estado de la Respuesta: ${response.statusCode}")
        println("Cuerpo de la Respuesta: ${response.body}")
    } catch (e: HttpException) {
        println("La solicitud falló con el error: ${e.message}")
    }

    runBlocking {
        val nonBlockingRequest = Request(
            url = "https://rickandmortyapi.com/api/character/3",
            method = HttpMethod.GET
        )
        val response = client.executeAsync(nonBlockingRequest)
        println("Código de Estado de la Respuesta NB: ${response.statusCode}")
        println("Cuerpo de la Respuesta NB: ${response.body}")
    }
}