package br.edu.ifsp.memora_app.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class DeckSyncDTO(
    val id: String,
    val name: String,
    val description: String?,
    val createdById: String,
    val fields: List<FieldDTO>,
    val cards: List<CardDTO>,
    val version: Int? = null
)

@Serializable
data class FieldDTO(
    val id: String,
    val name: String,
    val type: String
)

@Serializable
data class CardDTO(
    val id: String,
    val difficulty: Int,
    val index: Int,
    val fieldValues: List<FieldValueDTO>
)

@Serializable
data class FieldValueDTO(
    val fieldId: String,
    val value: String
)