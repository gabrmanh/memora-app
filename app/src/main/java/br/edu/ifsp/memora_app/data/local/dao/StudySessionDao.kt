package br.edu.ifsp.memora_app.data.local.dao

import androidx.room.*
import br.edu.ifsp.memora_app.domain.user.StudySession

@Dao
interface StudySessionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(session: StudySession)

    @Update
    suspend fun update(session: StudySession)

    @Delete
    suspend fun delete(session: StudySession)

    @Query("SELECT * FROM study_sessions WHERE id = :sessionId")
    suspend fun getById(sessionId: String): StudySession?

    @Query("SELECT * FROM study_sessions WHERE userId = :userId")
    suspend fun getSessionsForUser(userId: String): List<StudySession>

    @Query("SELECT * FROM study_sessions WHERE deckId = :deckId")
    suspend fun getSessionsForDeck(deckId: String): List<StudySession>
}
