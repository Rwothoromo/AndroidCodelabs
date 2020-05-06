package com.example.roomwordsample

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * The Entity which represents the SQLite table for words.
 * Each property in the class represents a column in the table.
 * Room will ultimately use these properties to both create the table
 * and instantiate objects from rows in the database.
 *
 * To make the Word class meaningful to a Room database, we annotate it.
 * Annotations identify how each part of this class relates to an entry in the database.
 * Room uses this information to generate code.
 *
 * Each @Entity class represents a SQLite table.
 * Annotate your class declaration to indicate that it's an entity.
 * You can specify the name of the table if you want it to be different from the name of the class.
 *
 * Every entity needs an @PrimaryKey. Consider the next line;
 * class Word(@PrimaryKey @ColumnInfo(name = "word") val word: String)
 * Simply putting @PrimaryKey above would make each word act as its own primary key
 *
 * Alternatively:
 * class Word(
 * @PrimaryKey(autoGenerate = true) val id: Int,
 * @ColumnInfo(name = "word") val word: String
 * )
 *
 * @ColumnInfo specifies the name of the column in the table
 * if you want it to be different from the name of the member variable.
 *
 * Every property that's stored in the database needs to have public visibility, which is the Kotlin default.
 */
@Entity(tableName = "words")
class Word(@PrimaryKey @ColumnInfo(name = "word") val word: String)
