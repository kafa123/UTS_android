package com.example.uts_android.Adapter

import com.example.uts_android.database.Movies
import com.google.firebase.auth.FirebaseAuth

interface UserOnClick {
    fun addBookmark(movies: Movies)
}