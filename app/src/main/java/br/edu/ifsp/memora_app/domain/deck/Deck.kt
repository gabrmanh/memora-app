package br.edu.ifsp.memora_app.domain.deck

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import br.edu.ifsp.memora_app.domain.user.User

@Entity(
    tableName = "decks",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["createdBy"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Deck(
    @PrimaryKey val id: String,
    val name: String,
    val description: String?,
    val createdBy: String
)