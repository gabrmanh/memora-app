package br.edu.ifsp.memora_app.data.remote.api

import br.edu.ifsp.memora_app.data.remote.dto.DeckPageResponse
import br.edu.ifsp.memora_app.data.remote.dto.DeckSummaryDto
import br.edu.ifsp.memora_app.data.remote.dto.UserDTO
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

object DeckApi {
    suspend fun getDecks(
        search: String? = null,
        page: Int = 0,
        size: Int = 10
    ): List<DeckSummaryDto> {

        val response = ApiClient.http.get("${ApiClient.BASE_URL}/decks") {
            url {
                parameters.append("page", page.toString())
                parameters.append("size", size.toString())
                search?.let { parameters.append("search", it) }
            }
        }

        val pageResponse: DeckPageResponse = response.body()
        return pageResponse.content
    }

    suspend fun getUsersInDeck(deckId: String): List<UserDTO> {
        return ApiClient.http.get("${ApiClient.BASE_URL}/decks/$deckId/users")
            .body()
    }

    suspend fun getDeckVersion(deckId: String): Int {
        return ApiClient.http.get("${ApiClient.BASE_URL}/decks/$deckId/version")
            .body()
    }
}