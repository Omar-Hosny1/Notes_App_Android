package com.example.quicknotesapp.model

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.quicknotesapp.database.NoteDatabaseDao
import com.example.quicknotesapp.homescreen.HomeViewModel

class NotesViewModelFactory(
    private val dataSource: NoteDatabaseDao,
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NotesViewModel::class.java)) {
            return NotesViewModel(application, dataSource) as T
        }
        throw Exception("Unknown ViewModel")
    }
}