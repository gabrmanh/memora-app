package br.edu.ifsp.memora_app.data.local.dao

import androidx.room.*
import br.edu.ifsp.memora_app.domain.deck.Deck

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
}