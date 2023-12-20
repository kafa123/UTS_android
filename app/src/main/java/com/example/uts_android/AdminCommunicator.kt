package com.example.uts_android

import com.example.uts_android.database.Movies

interface AdminCommunicator {
    fun passData(movies: Movies)
}