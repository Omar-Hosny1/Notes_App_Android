package com.example.quicknotesapp.model

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.example.quicknotesapp.database.Note
import com.example.quicknotesapp.database.NoteDatabaseDao
import kotlinx.coroutines.*

class NotesViewModel(application: Application, val database: NoteDatabaseDao) :
    AndroidViewModel(application) {
    val notes = database.getAllNotes()

    private val viewModelJob = Job()
    private val ioScope = CoroutineScope(viewModelJob + Dispatchers.IO)


    fun addNote(note: Note) {
        ioScope.launch {
            database.addNote(note)
        }
    }

    fun removeNote(id: Long) {
        ioScope.launch {
            database.removeNote(id)
        }
    }

    fun updateNote(note: Note) {
        ioScope.launch {
            database.updateNote(note)
        }
    }

    fun getNote(id: Long): Note {
        var foundedNote: Note? = null
        ioScope.launch {
            val note = database.getNote(id)
            foundedNote = note.value
        }
        return foundedNote ?: Note(1, "NOT WORKING")
    }

    private fun updateNotes(newNotes: ArrayList<Note>) {
//        _notes.value = newNotes
    }

    val noItemTextVisible = Transformations.map(notes) {
        it?.isEmpty()
    }
    val urQNTextVisible = Transformations.map(notes) {
        it?.isNotEmpty()
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}