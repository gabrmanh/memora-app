package br.edu.ifsp.memora_app.data.local.dto

import br.edu.ifsp.memora_app.domain.deck.Deck

data class DeckWithStatus(
    val deck: Deck,
    val status: DeckStatus,
    val pendingCount: Int,
    val totalCards: Int
)

enum class DeckStatus {
    PENDING,
    WAITING,
    NEW,
    FINISHED
}
