package br.edu.ifsp.memora_app.data.local.dto

data class StudySessionInfo(
    val deckId: String,
    val deckName: String,
    val cardsReady: Int,
    val nextCardUnlockTime: Long?,
    val totalCards: Int,
    val newCards: Int,
    val learningCards: Int,
    val reviewingCards: Int,
    val masteredCards: Int
)
