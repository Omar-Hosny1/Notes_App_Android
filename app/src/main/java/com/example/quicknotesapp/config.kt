package com.example.quicknotesapp

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import kotlin.random.Random

fun generateNewId(): Long {
    val id = Random.Default.nextLong()
    return id
}


@BindingAdapter("setVesibility")
fun TextView.setVis(isEmpty: Boolean) {
    isEmpty.let {
        if (isEmpty == true) {
            visibility = View.VISIBLE
        }else{
            visibility = View.INVISIBLE
        }
    }
}