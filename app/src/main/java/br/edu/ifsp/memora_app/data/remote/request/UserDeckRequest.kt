package br.edu.ifsp.memora_app.data.remote.request

import kotlinx.serialization.Serializable

@Serializable
data class UserDeckRequest(
    val userEmail: String,
    val deckId: String,
    val creatorId: String
)