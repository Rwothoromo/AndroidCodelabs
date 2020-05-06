package com.example.roomwordsample

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * This class gets the Application as a parameter and extends AndroidViewModel.
 */
class WordViewModel(application: Application) : AndroidViewModel(application) {

    // private member variable to hold a reference to the repository
    private val repository: WordRepository

    // Using LiveData and caching what getAlphabetizedWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.

    // public LiveData member variable to cache the list of words
    val allWords: LiveData<List<Word>>

    init {
        // get a reference to the WordDao from the WordRoomDatabase
        val wordsDao = WordRoomDatabase.getDatabase(application, viewModelScope).wordDao()

        // constructed the WordRepository based on the WordRoomDatabase
        repository = WordRepository(wordsDao)

        // initialized the allWords LiveData using the repository
        allWords = repository.allWords
    }

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     *
     * This wrapper method calls the Repository's insert() method.
     * In this way, the implementation of insert() is encapsulated from the UI.
     * We don't want insert to block the main thread,
     * so we're launching a new coroutine and calling the repository's insert,
     * which is a suspend function.
     *
     * ViewModels have a coroutine scope based on their life cycle called viewModelScope, which we use here.
     */
    fun insert(word: Word) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(word)
    }
}
