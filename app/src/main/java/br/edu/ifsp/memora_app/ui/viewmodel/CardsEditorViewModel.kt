package br.edu.ifsp.memora_app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.edu.ifsp.memora_app.data.local.dao.CardDao
import br.edu.ifsp.memora_app.data.local.dao.CardProgressDao
import br.edu.ifsp.memora_app.data.local.dao.FieldDao
import br.edu.ifsp.memora_app.data.local.dao.FieldValueDao
import br.edu.ifsp.memora_app.domain.deck.Card
import br.edu.ifsp.memora_app.domain.deck.CardProgress
import br.edu.ifsp.memora_app.domain.deck.CardState
import br.edu.ifsp.memora_app.domain.deck.Field
import br.edu.ifsp.memora_app.domain.deck.FieldValue
import br.edu.ifsp.memora_app.ui.config.SessionManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

class CardsEditorViewModel(
    private val cardDao: CardDao,
    private val fieldDao: FieldDao,
    private val fieldValueDao: FieldValueDao,
    private val cardProgressDao: CardProgressDao,
    private val deckId: String
) : ViewModel() {

    val cards: StateFlow<List<Card>> = cardDao.getCardsForDeck(deckId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val fields: StateFlow<List<Field>> = fieldDao.getFieldsForDeck(deckId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun getFieldValuesForCard(cardId: String): Flow<List<FieldValue>> {
        return fieldValueDao.getFieldValuesForCard(cardId)
    }

    fun addCard() {
        viewModelScope.launch {
            val currentCards = cards.value
            val newIndex = currentCards.size

            val newCard = Card(
                id = UUID.randomUUID().toString(),
                index = newIndex,
                deckId = deckId
            )

            cardDao.insert(newCard)

            // Create CardProgress for the new card
            val cardProgress = CardProgress(
                id = UUID.randomUUID().toString(),
                cardId = newCard.id,
                userId = SessionManager.requireUserId(),
                state = CardState.NEW,
                interval = 0,
                repetitions = 0,
                nextReviewDate = System.currentTimeMillis(),  // Available immediately
                lastReviewDate = null,
                attemptsInSession = 0,
                totalCorrect = 0,
                totalIncorrect = 0
            )

            cardProgressDao.insert(cardProgress)
        }
    }

    fun deleteCard(card: Card) {
        viewModelScope.launch {
            cardDao.delete(card)
            reindexCards()
        }
    }

    private suspend fun reindexCards() {
        val allCards = cardDao.getCardsForDeckSync(deckId).sortedBy { it.index }
        allCards.forEachIndexed { index, card ->
            if (card.index != index) {
                cardDao.update(card.copy(index = index))
            }
        }
    }
}