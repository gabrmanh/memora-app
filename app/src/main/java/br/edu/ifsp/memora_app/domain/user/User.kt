package br.edu.ifsp.memora_app.domain.user

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val userId: String,
    val name: String,
    val email: String,
    val passwordHash: String
)