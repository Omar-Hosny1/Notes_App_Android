package com.example.quicknotesapp.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface NoteDatabaseDao {
    @Query("SELECT * FROM notes_table")
    fun getAllNotes(): LiveData<List<Note>>

    @Insert
    fun addNote(note: Note)

    @Query("DELETE FROM notes_table WHERE id = :id")
    fun removeNote(id: Long)

    @Update
    fun updateNote(note: Note)

    //    @Query("SELECT * FROM notes_table WHERE id = :id")
//    fun getNote(id: Long): Note
    @Query("SELECT * FROM notes_table WHERE id = :id")
    fun getNote(id: Long): LiveData<Note>
}