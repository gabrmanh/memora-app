package br.edu.ifsp.memora_app.data.remote.api

import br.edu.ifsp.memora_app.data.remote.dto.UserDTO
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

object UserApi {
    suspend fun syncUserData(
        userId: String,
        name: String? = null,
        email: String? = null
    ): UserDTO {
        return ApiClient.http.post("${ApiClient.BASE_URL}/users/sync") {
            url {
                parameters.append("userId", userId)
                name?.let { parameters.append("name", it) }
                email?.let { parameters.append("email", it) }
            }
            contentType(ContentType.Application.Json)
        }.body()
    }
}