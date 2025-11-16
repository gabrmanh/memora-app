package br.edu.ifsp.memora_app.data.remote.api

import br.edu.ifsp.memora_app.data.remote.dto.*
import br.edu.ifsp.memora_app.data.remote.request.LoginRequest
import br.edu.ifsp.memora_app.data.remote.request.RegisterRequest
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

object AuthApi {
    suspend fun register(req: RegisterRequest): UserDTO {
        return ApiClient.http.post("${ApiClient.BASE_URL}/auth/register") {
            contentType(ContentType.Application.Json)
            setBody(req)
        }.body()
    }

    suspend fun login(req: LoginRequest): UserDTO {
        val user = ApiClient.http.post("${ApiClient.BASE_URL}/auth/login") {
            contentType(ContentType.Application.Json)
            setBody(req)
        }.body<UserDTO>()

        ApiClient.setBasicAuth(req.email, req.password)

        return user
    }
}