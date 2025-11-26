package br.edu.ifsp.memora_app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.edu.ifsp.memora_app.domain.deck.CardProgress
import kotlinx.coroutines.flow.Flow

@Dao
interface CardProgressDao {
    @Query("SELECT * FROM card_progress WHERE userId = :userId AND nextReviewDate <= :currentTime")
    fun getDueCards(userId: String, currentTime: Long): Flow<List<CardProgress>>

    @Query("""SELECT * FROM card_progress 
        WHERE userId = :userId 
        AND cardId IN (SELECT c.id FROM cards c WHERE deckId = :deckId)""")
    fun getCardProgressForDeck(userId: String, deckId: String): Flow<List<CardProgress>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(progress: CardProgress)
}
