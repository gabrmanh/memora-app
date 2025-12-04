package br.edu.ifsp.memora_app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.edu.ifsp.memora_app.data.local.dao.CardProgressDao
import br.edu.ifsp.memora_app.data.local.dao.DeckDao
import br.edu.ifsp.memora_app.data.local.dto.StudySessionInfo
import br.edu.ifsp.memora_app.domain.deck.CardState
import br.edu.ifsp.memora_app.ui.config.SessionManager
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class StudySessionStartViewModel(
    deckDao: DeckDao,
    cardProgressDao: CardProgressDao,
    private val deckId: String
) : ViewModel() {

    val sessionInfo: StateFlow<StudySessionInfo?> = combine(
        deckDao.getDeckByIdFlow(deckId),
        cardProgressDao.getAllProgressForDeck(SessionManager.requireUserId(), deckId)
    ) { deck, allProgress ->
        if (deck == null) return@combine null

        val currentTime = System.currentTimeMillis()
        val cardsReady = allProgress.count { it.nextReviewDate <= currentTime }
        val nextUnlock = allProgress
            .filter { it.nextReviewDate > currentTime }
            .minOfOrNull { it.nextReviewDate }

        val newCards = allProgress.count { it.state == CardState.NEW }
        val learningCards = allProgress.count { it.state == CardState.LEARNING }
        val reviewingCards = allProgress.count { it.state == CardState.REVIEWING }
        val masteredCards = allProgress.count { it.state == CardState.MASTERED }

        StudySessionInfo(
            deckId = deckId,
            deckName = deck.name,  // Always use current name
            cardsReady = cardsReady,
            nextCardUnlockTime = nextUnlock,
            totalCards = allProgress.size,
            newCards = newCards,
            learningCards = learningCards,
            reviewingCards = reviewingCards,
            masteredCards = masteredCards
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )
}
