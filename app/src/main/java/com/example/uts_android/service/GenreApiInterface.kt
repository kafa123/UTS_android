package com.example.uts_android.service

import com.example.uts_android.model.GenreResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GenreApiInterface {
    @GET("/3/genre/movie/list")
    fun getGenres(@Query("api_key") apiKey: String): Call<GenreResponse>
}