package br.edu.ifsp.memora_app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.edu.ifsp.memora_app.data.local.dao.CardDao
import br.edu.ifsp.memora_app.data.local.dao.CardProgressDao
import br.edu.ifsp.memora_app.data.local.dao.FieldDao
import br.edu.ifsp.memora_app.data.local.dao.FieldValueDao
import br.edu.ifsp.memora_app.data.local.dto.StudyCard
import br.edu.ifsp.memora_app.data.local.dto.StudyState
import br.edu.ifsp.memora_app.domain.AnswerResult
import br.edu.ifsp.memora_app.domain.SpacedRepetitionAlgorithm
import br.edu.ifsp.memora_app.domain.deck.FieldRole
import br.edu.ifsp.memora_app.ui.config.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.minutes

class StudySessionViewModel(
    private val cardDao: CardDao,
    private val cardProgressDao: CardProgressDao,
    private val fieldDao: FieldDao,
    private val fieldValueDao: FieldValueDao,
    private val deckId: String
) : ViewModel() {

    private val _studyState = MutableStateFlow<StudyState>(StudyState.Loading)
    val studyState: StateFlow<StudyState> = _studyState.asStateFlow()

    private val _cardsRemaining = MutableStateFlow(0)
    val cardsRemaining: StateFlow<Int> = _cardsRemaining.asStateFlow()

    private val _totalCards = MutableStateFlow(0)
    val totalCards: StateFlow<Int> = _totalCards.asStateFlow()

    private var studyQueue: MutableList<StudyCard> = mutableListOf()
    private var currentCard: StudyCard? = null
    private var currentAttempt = 1
    private var cardsStudied = 0

    init {
        loadStudyCards()
    }

    private fun loadStudyCards() {
        viewModelScope.launch {
            val userId = SessionManager.requireUserId()
            val currentTime = System.currentTimeMillis()

            // Get due cards
            val dueProgress = cardProgressDao.getDueCards(userId, currentTime).first()
                .filter { progress ->
                    // Verify card belongs to this deck
                    cardDao.getCardById(progress.cardId)?.deckId == deckId
                }

            // Load full study cards
            studyQueue = dueProgress.mapNotNull { progress ->
                val card = cardDao.getCardById(progress.cardId) ?: return@mapNotNull null
                val fields = fieldDao.getFieldsForDeckSync(deckId)
                val fieldValues = fieldValueDao.getFieldValuesForCardSync(card.id)

                val frontFields = fields.filter { it.role == FieldRole.FRONT }
                    .mapNotNull { field ->
                        val value = fieldValues.find { it.fieldId == field.id }?.value
                        if (value != null) field to value else null
                    }

                val answerFields = fields.filter { it.role == FieldRole.ANSWER }
                    .mapNotNull { field ->
                        val value = fieldValues.find { it.fieldId == field.id }?.value
                        if (value != null) field to value else null
                    }

                StudyCard(card, progress, frontFields, answerFields)
            }.toMutableList()

            _totalCards.value = studyQueue.size
            _cardsRemaining.value = studyQueue.size

            if (studyQueue.isNotEmpty()) {
                showNextCard()
            } else {
                _studyState.value = StudyState.Completed(0)
            }
        }
    }

    private fun showNextCard() {
        if (studyQueue.isEmpty()) {
            _studyState.value = StudyState.Completed(cardsStudied)
            return
        }

        currentCard = studyQueue.removeAt(0)
        currentAttempt = 1
        _cardsRemaining.value = studyQueue.size

        _studyState.value = StudyState.ShowingQuestion(
            card = currentCard!!,
            currentAttempt = currentAttempt
        )
    }

    fun submitAnswer(userAnswer: String) {
        val card = currentCard ?: return

        // Check if answer is correct (case-insensitive, trimmed)
        val correctAnswers = card.answerFields.map { it.second.trim().lowercase() }
        val isCorrect = correctAnswers.any {
            userAnswer.trim().lowercase().contains(it) ||
                    it.contains(userAnswer.trim().lowercase())
        }

        _studyState.value = StudyState.ShowingAnswer(
            card = card,
            userAnswer = userAnswer,
            isCorrect = isCorrect,
            currentAttempt = currentAttempt
        )
    }

    fun nextCard() {
        val state = _studyState.value as? StudyState.ShowingAnswer ?: return
        val card = state.card

        if (!state.isCorrect && currentAttempt == 1) {
            currentAttempt = 2
            _studyState.value = StudyState.ShowingQuestion(
                card = card,
                currentAttempt = 2
            )
            return
        }

        val result = when {
            state.isCorrect && currentAttempt == 1 -> AnswerResult.FIRST_CORRECT
            state.isCorrect && currentAttempt == 2 -> AnswerResult.SECOND_CORRECT
            else -> AnswerResult.WRONG_BOTH
        }

        viewModelScope.launch {
            val updatedProgress = SpacedRepetitionAlgorithm.processAnswer(
                card.progress,
                result
            )
            cardProgressDao.update(updatedProgress)

            if (updatedProgress.nextReviewDate <= System.currentTimeMillis() + 15.minutes.inWholeMilliseconds) {
                studyQueue.add(card.copy(progress = updatedProgress))
            }

            cardsStudied++
            showNextCard()
        }
    }

    fun endSession() {
        // Session ended, cleanup if needed
    }
}
