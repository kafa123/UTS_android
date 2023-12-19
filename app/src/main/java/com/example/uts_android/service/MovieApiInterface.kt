package com.example.uts_android.service

import com.example.uts_android.model.MovieResponse
import retrofit2.Call
import retrofit2.http.GET

interface MovieApiInterface {
    @GET("/3/movie/popular?api_key=50e9db748073c51a8b47e5d3ed6d5f18")

    fun getMovieLit():Call<MovieResponse>
}