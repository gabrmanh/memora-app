package br.edu.ifsp.memora_app.data.remote.api

import br.edu.ifsp.memora_app.data.remote.dto.DeckSyncDTO
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

object DeckSyncApi {
    suspend fun syncDeck(deck: DeckSyncDTO): DeckSyncDTO {
        return ApiClient.http.post("${ApiClient.BASE_URL}/sync/deck") {
            contentType(ContentType.Application.Json)
            setBody(deck)
        }.body()
    }

    suspend fun getDeck(deckId: String): DeckSyncDTO {
        return ApiClient.http.get("${ApiClient.BASE_URL}/sync/deck") {
            url { parameters.append("deckId", deckId) }
        }.body()
    }
}