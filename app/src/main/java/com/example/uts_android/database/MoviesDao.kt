package com.example.uts_android.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface MoviesDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(movies: Movies)

    @Update
    fun update(movies: Movies)

    @Delete
    fun delete(movies: Movies)

    @Query("SELECT * from moviedb")
    fun allMovies(): LiveData<List<Movies>>
}