package br.edu.ifsp.memora_app.data.local

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import br.edu.ifsp.memora_app.domain.deck.*
import br.edu.ifsp.memora_app.domain.user.*
import br.edu.ifsp.memora_app.data.local.dao.*

@Database(
    entities = [
        Deck::class,
        Card::class,
        Field::class,
        FieldValue::class,
        Answer::class,
        StudySession::class,
        User::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun deckDao(): DeckDao
    abstract fun cardDao(): CardDao
    abstract fun fieldDao(): FieldDao
    abstract fun fieldValueDao(): FieldValueDao
    abstract fun answerDao(): AnswerDao
    abstract fun studySessionDao(): StudySessionDao
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "memora.db"
                )
                    .fallbackToDestructiveMigration(false)
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}
