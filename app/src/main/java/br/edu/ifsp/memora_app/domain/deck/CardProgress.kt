package br.edu.ifsp.memora_app.domain.deck

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import br.edu.ifsp.memora_app.domain.user.User

@Entity(
    tableName = "card_progress",
    foreignKeys = [
        ForeignKey(
            entity = Card::class,
            parentColumns = ["id"],
            childColumns = ["cardId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = User::class,          // Replace with your actual user entity class
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["cardId"]),
        Index(value = ["userId"]),
        Index(value = ["cardId", "userId"], unique = true)
    ]
)
data class CardProgress(
    @PrimaryKey val id: String,
    val cardId: String,
    val userId: String,
    val state: CardState,
    val interval: Int = 0,
    val repetitions: Int = 0,
    val nextReviewDate: Long,
    val lastReviewDate: Long? = null,
    val attemptsInSession: Int = 0,
    val totalCorrect: Int = 0,
    val totalIncorrect: Int = 0
)

enum class CardState {
    NEW,           // Never studied
    LEARNING,      // In initial learning phase (2 chances)
    REVIEWING,     // In spaced repetition phase
    MASTERED       // Marked as done (interval >= 180 days)
}