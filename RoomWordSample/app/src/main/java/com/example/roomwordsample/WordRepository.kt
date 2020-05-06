package com.example.roomwordsample

import androidx.lifecycle.LiveData

/**
 * Declares the DAO as a private property in the constructor.
 * Pass in the DAO instead of the whole database, because you only need access to the DAO
 * since the DAO contains all the read/write methods for the database.
 * There's no need to expose the entire database to the repository.
 *
 * Repositories are meant to mediate between different data sources.
 * Here, you only have one data source.
 * For a more complex implementation, see https://github.com/android/architecture-components-samples/tree/master/BasicSample
 */
class WordRepository(private val wordDao: WordDao) {

    // The list of words is a public property. It's initialized by getting the LiveData list of words from Room;
    // we can do this because of how we defined the getAlphabetizedWords method.
    // Room executes all queries on a separate thread.
    // Then observed LiveData will notify the observer on the main thread when the data has changed.
    val allWords: LiveData<List<Word>> = wordDao.getAlphabetizedWords()

    /**
     * The suspend modifier tells the compiler that this needs to be called from a coroutine or another suspending function.
     */
    suspend fun insert(word: Word) {
        wordDao.insert(word)
    }
}
