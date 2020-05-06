package com.example.roomwordsample

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 * WordDao is an interface; DAOs must either be interfaces or abstract classes.
 * The @Dao annotation identifies it as a DAO class for Room.
 */
@Dao
interface WordDao {

    /**
     * A method to get all the words.
     * Originally:
     * fun getAlphabetizedWords(): List<Word>
     *
     * @return an observable List of Words in ascending order.
     */
    @Query("SELECT * from words ORDER BY word ASC")
    fun getAlphabetizedWords(): LiveData<List<Word>>

    /**
     * Declares a suspend function to insert one word.
     *
     * The @Insert annotation is a special DAO method annotation where you don't have to provide any SQL!
     * There are also @Delete and @Update annotations for deleting and updating rows.
     *
     * The selected on conflict strategy ignores a new word if it's exactly the same as one already in the list.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(word: Word)

    /**
     * Declares a suspend function to delete all the words.
     *
     * There is no convenience annotation for deleting multiple entities,
     * so it's annotated with the generic @Query.
     * @Query requires that you provide a SQL query as a string parameter to the annotation,
     * allowing for complex read queries and other operations.
     */
    @Query("DELETE FROM words")
    suspend fun deleteAll()
}
