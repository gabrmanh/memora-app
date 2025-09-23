package br.edu.ifsp.memora_app.domain.deck

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "fields",
    foreignKeys = [
        ForeignKey(
            entity = Deck::class,
            parentColumns = ["id"],
            childColumns = ["deckId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Field(
    @PrimaryKey val id: String,
    val name: String,
    val type: String,
    val deckId: String
)