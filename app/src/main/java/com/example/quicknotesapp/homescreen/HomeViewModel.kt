package com.example.quicknotesapp.homescreen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.quicknotesapp.database.NoteDatabaseDao


// we pass the application to access application resources like strings...
class HomeViewModel : ViewModel() {

    private val _navigateToNoteForm = MutableLiveData<Boolean>()
    val navigateToNoteForm: LiveData<Boolean>
        get() = _navigateToNoteForm

    fun doneNavigatingToNoteForm() {
        _navigateToNoteForm.value = false
    }

    fun onAddNote() {
        _navigateToNoteForm.value = true
    }
}