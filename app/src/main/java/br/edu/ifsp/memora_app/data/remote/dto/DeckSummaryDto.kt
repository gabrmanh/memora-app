package br.edu.ifsp.memora_app.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class DeckSummaryDto(
    val id: String,
    val name: String,
    val description: String?,
    val cardCount: Long
)

@Serializable
data class DeckPageResponse(
    val content: List<DeckSummaryDto>,
    val totalPages: Int,
    val totalElements: Long,
    val number: Int
)
