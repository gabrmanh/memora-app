package br.edu.ifsp.memora_app.data.local.dao

import androidx.room.*
import br.edu.ifsp.memora_app.domain.deck.Deck
import kotlinx.coroutines.flow.Flow

@Dao
interface DeckDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(deck: Deck)

    @Update
    suspend fun update(deck: Deck)

    @Delete
    suspend fun delete(deck: Deck)

    @Query("SELECT * FROM decks WHERE id = :deckId")
    suspend fun getById(deckId: String): Deck?

    @Query("SELECT * FROM decks WHERE createdBy = :userId")
    suspend fun getDecksByUser(userId: String): List<Deck>

    @Query("SELECT * FROM decks")
    suspend fun getAll(): List<Deck>

    @Query("""
        SELECT d.*, 
               COUNT(c.id) as totalCards,
               COUNT(CASE WHEN cp.nextReviewDate <= :currentTime THEN 1 END) as pendingCount,
               COUNT(CASE WHEN cp.state = 'NEW' OR cp.id IS NULL THEN 1 END) as newCount,
               COUNT(CASE WHEN cp.state = 'MASTERED' THEN 1 END) as masteredCount
        FROM decks d
        LEFT JOIN cards c ON c.deckId = d.id
        LEFT JOIN card_progress cp ON cp.cardId = c.id AND cp.userId = :userId
        GROUP BY d.id
    """)
    fun getDecksWithStats(userId: String, currentTime: Long): Flow<List<DeckStats>>
}

data class DeckStats(
    @Embedded val deck: Deck,
    val totalCards: Int,
    val pendingCount: Int,
    val newCount: Int,
    val masteredCount: Int
)