package com.example.uts_android.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.uts_android.database.Movies

class AdminViewModel:ViewModel() {
    val selectedMovie = MutableLiveData<Movies>()

    fun setSelectedMovie(movie: Movies) {
        selectedMovie.value = movie
    }
}