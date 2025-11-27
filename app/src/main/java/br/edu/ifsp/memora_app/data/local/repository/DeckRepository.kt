package br.edu.ifsp.memora_app.data.local.repository

import br.edu.ifsp.memora_app.data.local.dao.CardProgressDao
import br.edu.ifsp.memora_app.data.local.dao.DeckDao
import br.edu.ifsp.memora_app.data.local.dao.DeckStats
import br.edu.ifsp.memora_app.data.local.dto.DeckStatus
import br.edu.ifsp.memora_app.data.local.dto.DeckWithStatus
import br.edu.ifsp.memora_app.domain.deck.CardProgress
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class DeckRepository(
    private val deckDao: DeckDao,
    private val cardProgressDao: CardProgressDao
) {
    fun getDecksWithStatus(userId: String): Flow<List<DeckWithStatus>> {
        val currentTime = System.currentTimeMillis()

        return deckDao.getDecksWithStats(userId, currentTime).map { statsList ->
            statsList.map { stats ->
                DeckWithStatus(
                    deck = stats.deck,
                    status = determineStatus(stats),
                    pendingCount = stats.pendingCount,
                    totalCards = stats.totalCards
                )
            }
        }
    }

    private fun determineStatus(stats: DeckStats): DeckStatus {
        return when {
            stats.totalCards == 0 -> DeckStatus.NEW
            stats.masteredCount == stats.totalCards -> DeckStatus.FINISHED
            stats.pendingCount > 0 -> DeckStatus.PENDING
            stats.newCount == stats.totalCards -> DeckStatus.NEW
            else -> DeckStatus.WAITING
        }
    }

    suspend fun getDueCardsForDeck(userId: String, deckId: String): List<CardProgress> {
        val currentTime = System.currentTimeMillis()
        return cardProgressDao.getDueCards(userId, currentTime)
            .first()
            .filter { progress ->
                // Get card and verify it belongs to this deck
                // You'll need to add a deckId field to CardProgress or join with Card
                true // placeholder
            }
    }
}
