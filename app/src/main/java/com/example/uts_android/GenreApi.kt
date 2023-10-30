package com.example.uts_android

import com.example.uts_android.model.Genre
import com.example.uts_android.model.GenreResponse
import com.example.uts_android.service.GenreApiInterface
import com.example.uts_android.service.MovieApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GenreApi {

    interface GenreCallback {
        fun onGenresFetched(genres: List<Genre>)
        fun onError(error: String)
    }

    fun fetchGenres(callback: GenreCallback) {
        val movieApiService = MovieApiService().getInstance()
        val apiKey = "50e9db748073c51a8b47e5d3ed6d5f18" // Replace with your actual API key
        val call = movieApiService.create(GenreApiInterface::class.java).getGenres(apiKey)

        call.enqueue(object : Callback<GenreResponse> {
            override fun onResponse(call: Call<GenreResponse>, response: Response<GenreResponse>) {
                if (response.isSuccessful) {
                    val genreResponse = response.body()
                    val genres = genreResponse?.genres

                    if (genres != null) {
                        callback.onGenresFetched(genres)
                    } else {
                        callback.onError("No genres found")
                    }
                } else {
                    callback.onError("Failed to fetch genres")
                }
            }

            override fun onFailure(call: Call<GenreResponse>, t: Throwable) {
                callback.onError(t.message ?: "Unknown error occurred")
            }
        })
    }
}
