package br.edu.ifsp.memora_app.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserDTO(
    val id: String,
    val name: String,
    val email: String
)