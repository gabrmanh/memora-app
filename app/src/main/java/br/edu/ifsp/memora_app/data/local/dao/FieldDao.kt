package br.edu.ifsp.memora_app.data.local.dao

import androidx.room.*
import br.edu.ifsp.memora_app.domain.deck.Field
import kotlinx.coroutines.flow.Flow

@Dao
interface FieldDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(field: Field)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(fields: List<Field>)

    @Update
    suspend fun update(field: Field)

    @Delete
    suspend fun delete(field: Field)

    @Query("SELECT * FROM fields WHERE id = :fieldId")
    suspend fun getById(fieldId: String): Field?

    @Query("SELECT * FROM fields WHERE deckId = :deckId ORDER BY name ASC")
    fun getFieldsForDeck(deckId: String): Flow<List<Field>>
}