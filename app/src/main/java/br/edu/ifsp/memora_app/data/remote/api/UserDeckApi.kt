package br.edu.ifsp.memora_app.data.remote.api

import br.edu.ifsp.memora_app.data.remote.request.UserDeckRequest
import io.ktor.client.request.*
import io.ktor.http.*

object UserDeckApi {
    suspend fun registerUserToDeck(req: UserDeckRequest): Boolean {
        val response = ApiClient.http.post("${ApiClient.BASE_URL}/user-decks/register") {
            contentType(ContentType.Application.Json)
            setBody(req)
        }
        return response.status.isSuccess()
    }

    suspend fun removeUserFromDeck(req: UserDeckRequest): Boolean {
        val response = ApiClient.http.delete("${ApiClient.BASE_URL}/user-decks/register") {
            contentType(ContentType.Application.Json)
            setBody(req)
        }
        return response.status.isSuccess()
    }
}