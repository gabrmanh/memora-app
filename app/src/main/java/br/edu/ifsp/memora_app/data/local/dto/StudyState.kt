package br.edu.ifsp.memora_app.data.local.dto

import br.edu.ifsp.memora_app.domain.deck.Card
import br.edu.ifsp.memora_app.domain.deck.CardProgress
import br.edu.ifsp.memora_app.domain.deck.Field

sealed class StudyState {
    data object Loading : StudyState()
    data class ShowingQuestion(
        val card: StudyCard,
        val currentAttempt: Int = 1
    ) : StudyState()
    data class ShowingAnswer(
        val card: StudyCard,
        val userAnswer: String,
        val isCorrect: Boolean,
        val currentAttempt: Int
    ) : StudyState()
    data class Completed(val studiedCount: Int) : StudyState()
}

data class StudyCard(
    val card: Card,
    val progress: CardProgress,
    val frontFields: List<Pair<Field, String>>,  // Field to value
    val answerFields: List<Pair<Field, String>>  // Field to value
)
