package br.edu.ifsp.memora_app.data.local.dao

import androidx.room.*
import br.edu.ifsp.memora_app.domain.deck.Card
import kotlinx.coroutines.flow.Flow

@Dao
interface CardDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(card: Card)

    @Delete
    suspend fun delete(card: Card)

    @Update
    suspend fun update(card: Card)

    @Query("SELECT * FROM cards WHERE deckId = :deckId ORDER BY `index` ASC")
    fun getCardsForDeck(deckId: String): Flow<List<Card>>

    @Query("SELECT * FROM cards WHERE deckId = :deckId ORDER BY `index` ASC")
    suspend fun getCardsForDeckSync(deckId: String): List<Card>
}