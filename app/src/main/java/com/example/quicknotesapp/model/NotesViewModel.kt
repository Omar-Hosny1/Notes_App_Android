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

    suspend fun getNote(id: Long): Note {
        return withContext(ioScope.coroutineContext) {
            val deferredNote = async { database.getNote(id) }
            deferredNote.await() ?: Note(1, "NOT WORKING")
        }
    }

    val noItemTextVisible = Transformations.map(notes) {
        it?.isEmpty()
    }

    val yourQuickNotesTextVisible = Transformations.map(notes) {
        it?.isNotEmpty()
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}