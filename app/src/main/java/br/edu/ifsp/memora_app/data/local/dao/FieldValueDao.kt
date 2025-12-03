package br.edu.ifsp.memora_app.data.local.dao

import androidx.room.*
import br.edu.ifsp.memora_app.domain.deck.FieldValue
import kotlinx.coroutines.flow.Flow

@Dao
interface FieldValueDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(fieldValue: FieldValue)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(values: List<FieldValue>)

    @Update
    suspend fun update(fieldValue: FieldValue)

    @Delete
    suspend fun delete(fieldValue: FieldValue)

    @Query("""
        SELECT * FROM field_values 
        WHERE cardId = :cardId AND fieldId = :fieldId
    """)
    suspend fun get(cardId: String, fieldId: String): FieldValue?

    @Query("SELECT * FROM field_values WHERE cardId = :cardId")
    suspend fun getValuesForCard(cardId: String): List<FieldValue>

    @Query("SELECT * FROM field_values WHERE fieldId = :fieldId")
    suspend fun getValuesForField(fieldId: String): List<FieldValue>

    @Query("SELECT * FROM field_values WHERE cardId = :cardId")
    fun getFieldValuesForCard(cardId: String): Flow<List<FieldValue>>
}
