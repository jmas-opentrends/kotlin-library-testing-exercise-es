# Ejercicio: Reimplementar la libreria OkHttp

En esta tarea, crearás una versión simplificada de la librería OkHttp, un cliente HTTP muy utilizado para realizar solicitudes de red. Este ejercicio te ayudará a:

- Adquirir experiencia práctica con las características del lenguaje de Kotlin.
- Comprender el uso de coroutines para procesamiento asíncrono.
- Aprender a configurar y gestionar un proyecto Kotlin utilizando Gradle.
- Aplicar las mejores prácticas para estructurar librerías en Kotlin.
- Explorar tests unitarios y simulación de comportamientos con MockK y Kotest.

## Parte 1: Implementación

### Instrucciones

1. **Inicializa un Nuevo Proyecto Gradle**

   Abre tu terminal y ejecuta:

   ```bash
   gradle init --type kotlin-library

   ```

   Este comando inicializa un nuevo proyecto de librería en Kotlin con Gradle.

2. **Configura `build.gradle.kts` con las dependencias y plugins necesarios**
3. **Configura la Estructura del Proyecto**

   Puedes usar esta estructura como base

   ```
   src/
   ├── main/
   │   └── kotlin/
   │       └── com/example/http/
   │           ├── HttpClient.kt
   │           ├── Request.kt
   │           └── Response.kt
   └── test/
       └── kotlin/
           └── com/example/http/
               ├── HttpClientTest.kt
               ├── RequestTest.kt
               └── ResponseTest.kt

   ```

### Funcionalidades y APIs Esperadas

### Resumen

Construirás una librería de cliente HTTP simplificada con las siguientes características:

1. **Solicitudes HTTP**
   - Soporte para los métodos GET y POST.
   - Capacidad para establecer encabezados, parámetros de consulta y cuerpos de solicitud.
2. **Respuestas HTTP**
   - Recuperar códigos de estado, encabezados y contenido del cuerpo de la respuesta.
   - Analizar cuerpos de respuesta como cadenas de texto o como objetos JSON.
3. **Manejo de Errores**
   - Gestionar errores HTTP (4xx, 5xx).
   - Manejar excepciones de red de manera elegante.
4. **Procesamiento Asíncrono**
   - Usar coroutines para llamadas de red no bloqueantes.
   - Proveer APIs tanto síncronas como asíncronas.

### Especificaciones Detalladas de la API

1. **Clase Request**

   Necesitas crear una clase `Request` que encapsule los detalles de una solicitud HTTP. La clase debe soportar varios métodos HTTP (por ejemplo, GET, POST), permitir la configuración de encabezados y, opcionalmente, incluir un cuerpo de solicitud para métodos como POST.

   - **Propiedades a Incluir:**
     - `url`: La URL a la cual se hará la solicitud.
     - `method`: El método HTTP (por ejemplo, GET, POST).
     - `headers`: Un mapa para almacenar pares clave-valor de encabezados.
     - `body`: Contenido del cuerpo opcional para POST u otros métodos que acepten un cuerpo.
   - **Métodos Esperados:**
     - `addHeader(name: String, value: String)`: Agrega un nuevo encabezado a la solicitud.
     - `setBody(body: String)`: Establece o actualiza el contenido del cuerpo de la solicitud.

   > Sugerencia: Usa una data class para Request para facilitar la gestión de la inmutabilidad y la representación de datos en Kotlin.

2. **Clase Response**

   Implementa una clase `Response` que modele la respuesta del servidor a una `Request`. Esta clase debe capturar el código de estado de la respuesta, encabezados y cualquier contenido devuelto por el servidor.

   - **Propiedades a Incluir:**
     - `statusCode`: Entero que representa el código de estado HTTP (por ejemplo, 200, 404).
     - `headers`: Un mapa de encabezados devueltos por el servidor.
     - `body`: Contenido opcional devuelto por el servidor, que podría ser una cadena de texto o JSON.

   > Sugerencia: Considera hacer que el cuerpo de la respuesta sea nullable para representar casos en los que la respuesta puede no incluir contenido.

3. **Clase HttpClient**

   Diseña la clase principal `HttpClient` para gestionar el proceso de ejecutar solicitudes HTTP. Esta clase debe proporcionar APIs tanto síncronas como asíncronas para realizar llamadas de red.

   - **Métodos Esperados:**
     - `execute(request: Request)`: Una función síncrona que envía la solicitud y espera la respuesta.
     - `executeAsync(request: Request)`: Una función asíncrona (`suspend`) que envía la solicitud y devuelve la respuesta sin bloquear el hilo.

   > Sugerencia: Utiliza coroutines de Kotlin para implementar la función executeAsync para operaciones de E/S no bloqueantes.

4. **Enums y Excepciones**

   - **Enum HttpMethod**
     Crea un enum `HttpMethod` para representar los métodos HTTP que el cliente debería soportar, como GET y POST.
     - **Valores Requeridos del Enum:**
       - `GET`
       - `POST`
   - **Excepciones Personalizadas**
     Define una clase de excepción personalizada `HttpException` para gestionar escenarios de error. Esta excepción puede usarse para capturar y propagar errores encontrados durante la comunicación HTTP.
     - **Requisitos:**
       - Debería extender `Exception` e incluir un mensaje de error.
       - Considera agregar información adicional, como códigos de estado o tipos de error, para proporcionar más contexto en la excepción.

   > Sugerencia: Usa excepciones de manera moderada y proporciona mensajes de error significativos que ayuden a depurar el problema.

### Ejemplos de Uso

A continuación, se muestran algunos ejemplos que demuestran cómo usar la librería de cliente HTTP que vas a construir. Estos ejemplos resaltan diferentes funcionalidades, incluyendo realizar solicitudes HTTP, gestionar respuestas y utilizar APIs síncronas y asíncronas.

---

**Ejemplo 1: Envío de una Solicitud GET Básica**

En este ejemplo, se crea un objeto `Request` con el método GET y se usa la clase `HttpClient` para enviar la solicitud de manera síncrona.

```kotlin
fun main() {
    val client = HttpClient()

    // Crea una solicitud GET para obtener datos de una URL especificada
    val request = Request(
        url = "<https://api.example.com/data>",
        method = HttpMethod.GET
    )

    // Ejecuta la solicitud de manera síncrona
    val response = client.execute(request)

    // Imprime el código de estado y el contenido del cuerpo de la respuesta
    println("Código de Estado de la Respuesta: ${response.statusCode}")
    println("Cuerpo de la Respuesta: ${response.body}")
}

```

**Resultado Esperado:**
El objeto `Response` debería contener un código de estado (por ejemplo, 200) y el contenido del cuerpo devuelto por el servidor.

---

**Ejemplo 2: Envío de una Solicitud POST con Encabezados y Cuerpo**

Este ejemplo demuestra cómo enviar una solicitud POST con encabezados personalizados y un cuerpo en formato JSON.

```kotlin
fun main() {
    val client = HttpClient()

    // Crea una solicitud POST con encabezados y un cuerpo JSON
    val request = Request(
        url = "<https://api.example.com/create>",
        method = HttpMethod.POST
    )
    .addHeader("Content-Type", "application/json")
    .addHeader("Authorization", "Bearer your_token_here")
    .setBody("""{"name": "John Doe", "age": 30}""")

    // Ejecuta la solicitud y gestiona la respuesta
    val response = client.execute(request)

    // Imprime los detalles de la respuesta
    println("Código de Estado de la Respuesta: ${response.statusCode}")
    println("Encabezados de la Respuesta: ${response.headers}")
    println("Cuerpo de la Respuesta: ${response.body}")
}

```

**Resultado Esperado:**
La solicitud POST envía un cuerpo JSON con datos de usuario. La respuesta debería incluir información sobre el estado de la operación, encabezados y cualquier contenido devuelto por el servidor.

---

**Ejemplo 3: Manejo de Errores de Forma Elegante**

Este ejemplo ilustra cómo manejar errores HTTP usando un bloque try-catch con excepciones personalizadas.

```kotlin
fun main() {
    val client = HttpClient()

    try {
        // Intenta enviar una solicitud GET a una URL inexistente
        val request = Request(
            url = "<https://api.example.com/nonexistent>",
            method = HttpMethod.GET
        )

        // Ejecuta la solicitud
        val response = client.execute(request)
        println("Código de Estado de la Respuesta: ${response.statusCode}")
    } catch (e: HttpException) {
        // Gestiona las excepciones HTTP (por ejemplo, 404 o errores de red)
        println("La solicitud falló con el error: ${e.message}")
    }
}

```

**Resultado Esperado:**
La aplicación debería capturar la `HttpException` e imprimir un mensaje de error significativo, como "La solicitud falló con el error: 404 No Encontrado".

---

**Ejemplo 4: Realización de Solicitudes HTTP Asíncronas**

Aquí se muestra cómo usar el método `executeAsync` para realizar solicitudes HTTP no bloqueantes.

```kotlin
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    val client = HttpClient()

    // Crea una solicitud GET asíncrona
    val request = Request(
        url = "<https://api.example.com/async-data>",
        method = HttpMethod.GET
    )

    // Ejecuta la solicitud de manera asíncrona
    val response = client.executeAsync(request)

    // Imprime los detalles de la respuesta
    println("Código de Estado de la Respuesta (Asíncrona): ${response.statusCode}")
    println("Cuerpo de la Respuesta (Asíncrona): ${response.body}")
}
```

**Resultado Esperado:**
La solicitud asíncrona debería completarse sin bloquear el hilo principal. Los detalles de la respuesta deberían imprimirse una vez que la solicitud haya terminado.

---

### Pautas de Implementación

1. **Evita Librerías HTTP de Terceros**

   Para mantener este ejercicio centrado en las características del lenguaje de Kotlin, utiliza las APIs `HttpURLConnection` o `HttpClient` integradas en Java para realizar las llamadas de red en lugar de depender de librerías de terceros como OkHttp.

2. **Usa Coroutines de Kotlin**

   Aprovecha las coroutines de Kotlin para el procesamiento asíncrono. Esto proporcionará un enfoque moderno y no bloqueante para gestionar las solicitudes de red.

3. **Escribe Código Limpio y Mantenible**

   Adhiérete a las mejores prácticas de Kotlin y sigue las convenciones de nomenclatura estándar. Usa nombres significativos para las clases, métodos y propiedades, y evita hardcodear valores.

---

## Parte 2: Tests y Simulación de Comportamientos

### Requisitos para Configurar el Entorno de Tests

1. **Incluye las Dependencias Necesarias para las Tests**

   Asegúrate de que tu proyecto incluya dependencias tanto para los tests como para los frameworks de simulación de comportamientos. Puedes usar Kotest o kotlin-test para tests unitarios y una librería como MockK para simular objetos y verificar interacciones.

2. **Configura el Runner de Tests**

   Asegúrate de que tu entorno de tests esté configurado para usar un runner de tests compatible con Kotlin, como JUnit5, para ejecutar los test cases.

### **Escenarios de Tests Requeridos**

Crea tests unitarios que validen la funcionalidad de tu librería de cliente HTTP. Considera los siguientes escenarios:

1. **Tests de Creación de Solicitudes**

   Valida que la clase `Request` configure correctamente los encabezados, el método HTTP y el cuerpo. Esto incluye:

   - Asegurarse de que los encabezados se añadan correctamente utilizando el método `addHeader`.
   - Verificar que el cuerpo de la solicitud se configure correctamente con el método `setBody`.
   - Confirmar que el método HTTP y la URL se asignen correctamente en el objeto `Request`.

2. **Tests de Manejo de Respuestas**

   Prueba la clase `Response` para asegurarte de que analice y almacene correctamente:

   - El código de estado devuelto por el servidor.
   - Todos los encabezados incluidos en la respuesta.
   - El cuerpo de la respuesta, ya sea una cadena de texto, JSON o vacío.

3. **Tests de Manejo de Errores**

   Simula diferentes escenarios de error para probar cómo tu librería gestiona fallos, tales como:

   - Errores de red o timeouts.
   - URLs inválidas o malformadas.
   - Códigos de estado HTTP que indiquen errores del lado del cliente (4xx) o del lado del servidor (5xx).

4. **Tests de Llamadas Asíncronas**

   Escribe tests para el método `executeAsync`, asegurándote de que:

   - El método realice operaciones no bloqueantes correctamente.
   - Se mantenga el flujo de ejecución adecuado con coroutines.
   - El método pueda gestionar solicitudes concurrentes y devolver las respuestas esperadas.

Dado que esta es una librería que requiere comunicación con la red, simular las interacciones de red es crucial. Deberías usar MockK para simular solicitudes y respuestas HTTP. Esto te permitirá probar el comportamiento de tu cliente sin hacer realmente llamadas de red. Algunas áreas clave en las que deberías enfocarte al simular incluyen:

- Simular solicitudes y respuestas de red exitosas y fallidas.
- Simular diferentes códigos de respuesta y encabezados.
- Probar escenarios de timeout y errores de conexión.

### Ejemplos de Casos de Prueba

Aquí tienes una lista de ejemplos (estilo BDD) de tests que puedes ejecutar, en caso de que necesites inspiración:

**Tests de Request**

1. "should create a request with the correct URL and HTTP method"
2. "should add a header to the request correctly"
3. "should set the request body content successfully"
4. "should retain headers when modifying the request body"
5. "should override existing headers when adding a header with the same name"

**Tests de Response**

1. "should parse and return the response status code correctly"
2. "should store and retrieve headers from the response"
3. "should return the response body as a string"
4. "should handle empty response body gracefully"
5. "should correctly identify and handle HTTP status codes like 404 or 500"

**Tests de Errores**

1. "should throw HttpException for invalid URLs"
2. "should handle network timeouts gracefully"
3. "should return an error response for HTTP 4xx client errors"
4. "should return an error response for HTTP 5xx server errors"
5. "should retry the request a specified number of times in case of transient network errors"

**Tests de concurrencia**

1. "should execute asynchronous GET request successfully"
2. "should execute asynchronous POST request with headers and body"
3. "should handle asynchronous network errors correctly"
4. "should not block the main thread when executing an asynchronous request"
5. "should handle multiple asynchronous requests concurrently without interference"

---

¡Happy Kotling!
