package br.edu.ifsp.memora_app.data.local.dao

import androidx.room.*
import br.edu.ifsp.memora_app.domain.user.Answer

@Dao
interface AnswerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(answer: Answer)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(answers: List<Answer>)

    @Update
    suspend fun update(answer: Answer)

    @Delete
    suspend fun delete(answer: Answer)

    @Query("SELECT * FROM answers WHERE id = :answerId")
    suspend fun getById(answerId: String): Answer?

    @Query("SELECT * FROM answers WHERE studySessionId = :sessionId")
    suspend fun getAnswersForSession(sessionId: String): List<Answer>

    @Query("SELECT * FROM answers WHERE cardId = :cardId")
    suspend fun getAnswersForCard(cardId: String): List<Answer>
}
