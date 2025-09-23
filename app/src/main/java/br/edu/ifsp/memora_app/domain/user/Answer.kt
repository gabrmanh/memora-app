package br.edu.ifsp.memora_app.domain.user

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import br.edu.ifsp.memora_app.domain.deck.Card

@Entity(
    tableName = "answers",
    foreignKeys = [
        ForeignKey(
            entity = StudySession::class,
            parentColumns = ["id"],
            childColumns = ["studySessionId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Card::class,
            parentColumns = ["id"],
            childColumns = ["cardId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Answer(
    @PrimaryKey val id: String,
    val isCorrect: Boolean,
    val answerIndex: Int,
    val studySessionId: String,
    val cardId: String
)