package br.edu.ifsp.memora_app.data.remote.api


import android.util.Base64
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import io.ktor.client.request.header


object ApiClient {
    private var authHeader: String? = null

    fun setBasicAuth(email: String, password: String) {
        val raw = "$email:$password"
        val encoded = Base64.encodeToString(raw.toByteArray(), Base64.NO_WRAP)
        authHeader = "Basic $encoded"
    }

    fun clearAuth() {
        authHeader = null
    }

    val http = HttpClient(OkHttp) {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }

        install(Logging)

        // Automatically adds Authorization if exists
        install(DefaultRequest) {
            authHeader?.let { header("Authorization", it) }
        }
    }

    const val BASE_URL = "http://10.0.2.2:8080"
}