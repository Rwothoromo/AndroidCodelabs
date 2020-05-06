package com.example.roomwordsample

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Your Room database class must be abstract and extend RoomDatabase.
 * Usually, you only need one instance of a Room database for the whole app.
 *
 * @Database below annotates the class to be a Room Database with a table (entity) of the Word class
 * use the annotation parameters to declare the entities that belong in the database and set the version number.
 * Each entity corresponds to a table that will be created in the database.
 *
 * Database migrations are beyond the scope of this codelab, so we set exportSchema to false here to avoid a build warning.
 * In a real app, you should consider setting a directory for Room to use to export the schema
 * so you can check the current schema into your version control system.
 *
 * For migrations, see https://medium.com/androiddevelopers/understanding-migrations-with-room-f01e04b07929
 */
@Database(entities = arrayOf(Word::class), version = 1, exportSchema = false)
public abstract class WordRoomDatabase : RoomDatabase() {

    /**
     * The database exposes DAOs through an abstract "getter" method for each @Dao.
     */
    abstract fun wordDao(): WordDao

    companion object {
        // Singleton prevents multiple instances of database opening at the same time.
        @Volatile
        private var INSTANCE: WordRoomDatabase? = null

        /**
         * This will create the database the first time it's accessed,
         * using Room's database builder to create a RoomDatabase object in the application context
         * from the WordRoomDatabase class and names it "word_database".
         *
         * @param context: Context
         * @param scope: CoroutineScope
         *
         * @return singleton.
         */
        fun getDatabase(context: Context, scope: CoroutineScope): WordRoomDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WordRoomDatabase::class.java,
                    "word_database"
                )
                    .addCallback(WordDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }

    private class WordDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        /**
         * Add some data when the database is opened
         * Because you cannot do Room database operations on the UI thread,
         * onOpen() launches a coroutine on the IO Dispatcher.
         *
         *  If you only want to populate the database the first time the app is launched,
         *  you can override the onCreate()
         *
         * @param db: SupportSQLiteDatabase
         */
        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.wordDao())
                }
            }
        }

        suspend fun populateDatabase(wordDao: WordDao) {
            // Delete all content here.
            wordDao.deleteAll()

            // Add sample words.
            var word = Word("Hello")
            wordDao.insert(word)
            word = Word("World!")
            wordDao.insert(word)

            // TODO: Add your own words!
        }
    }
}
