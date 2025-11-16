package br.edu.ifsp.memora_app.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class FieldValueDto(val fieldId: String, val value: String)

@Serializable
data class CardDto(
    val id: String,
    val difficulty: Int,
    val index: Int,
    val fieldValues: List<FieldValueDto>
)

@Serializable
data class FieldDto(val id: String, val name: String, val type: String)

@Serializable
data class DeckDto(
    val id: String,
    val name: String,
    val description: String?,
    val createdById: String,
    val fields: List<FieldDto>,
    val cards: List<CardDto>
)