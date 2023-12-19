package com.example.uts_android

import com.example.uts_android.model.DataMovie
import com.example.uts_android.model.MovieResponse
import com.example.uts_android.service.MovieApiInterface
import com.example.uts_android.service.MovieApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieApi {
    public val dataMovieList = ArrayList<DataMovie>()

    interface MovieCallback {
        fun onMoviesFetched(Movies: List<DataMovie>)
        fun onError(error: String)
    }

    fun fetchMovies(callback: MovieCallback) {
        val movieApiService = MovieApiService().getInstance()
        val call = movieApiService.create(MovieApiInterface::class.java).getMovieLit()

        call.enqueue(object : Callback<MovieResponse> {
            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                if (response.isSuccessful) {
                    val movieResponse = response.body()
                    val movies = movieResponse?.results

                    if (movies != null) {
                        for (movie in movies) {

                            val newDataMovie = DataMovie(
                                movie.adult,
                                movie.backdropPath,
                                movie.genreIds,
                                movie.id,
                                movie.originalLanguage,
                                movie.originalTitle,
                                movie.overview,
                                movie.popularity,
                                movie.posterPath,
                                movie.releaseDate,
                                movie.title,
                                movie.video,
                                movie.voteAverage,
                                movie.voteCount
                            )
                            dataMovieList.add(newDataMovie)
                        }
                        callback.onMoviesFetched(dataMovieList)
                    }
                } else {
                }
            }

            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                // Handle failure
            }
        })
    }
}