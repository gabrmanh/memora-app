package br.edu.ifsp.memora_app.data.local.dto

import br.edu.ifsp.memora_app.domain.deck.Deck

data class DeckWithStatus(
    val deck: Deck,
    val status: DeckStatus
)

enum class DeckStatus {
    PENDING, WAITING, NEW, FINISHED
}