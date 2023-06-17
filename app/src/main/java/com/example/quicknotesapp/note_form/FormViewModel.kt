package com.example.quicknotesapp.note_form

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FormViewModel : ViewModel() {
    private val _navigateToHome = MutableLiveData<Boolean>()
    val navigateToHome: LiveData<Boolean>
        get() = _navigateToHome

    fun doneNavigatingToHome() {
        _navigateToHome.value = false
    }

    fun onBackPressed() {
        _navigateToHome.value = true
    }
}