package com.example.uts_android.Adapter

import com.example.uts_android.database.Movies

interface onClickAdmin {
    fun onEditClick(movie: Movies)
    fun onDeleteClick(movie: Movies)
}