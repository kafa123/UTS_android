package com.example.uts_android

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.uts_android.databinding.ActivityMainBinding
import com.example.uts_android.databinding.FragmentHomeBinding
import com.example.uts_android.model.DataMovie
import com.example.uts_android.model.MovieResponse
import com.example.uts_android.service.MovieApiInterface
import com.example.uts_android.service.MovieApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */



class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var MovieRecyclerView: RecyclerView
    private lateinit var dataMovieList: ArrayList<DataMovie>
    private lateinit var binding: FragmentHomeBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)


        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        dataMovieList = arrayListOf<DataMovie>()
//        fetchMovies()
        

        with(binding) {

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
                                MovieRecyclerView=topMoviesRecyclerView
                                val adapter=MovieAdapter(dataMovieList)
                                MovieRecyclerView.adapter=adapter
                            }
                        } else {
                        }
                    }
                    override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                        TODO("Not yet implemented")
                    }
                })

        }
        return view
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

//    fun fetchMovies() {
//        val movieApiService = MovieApiService().getInstance()
//        val call = movieApiService.create(MovieApiInterface::class.java).getMovieLit()
//
//        call.enqueue(object : Callback<MovieResponse> {
//            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
//                if (response.isSuccessful) {
//                    val movieResponse = response.body()
//                    val movies = movieResponse?.results
//
//                    if (movies != null) {
//                        for (movie in movies) {
//
//                            val newDataMovie = DataMovie(
//                                movie.adult,
//                                movie.backdropPath,
//                                movie.genreIds,
//                                movie.id,
//                                movie.originalLanguage,
//                                movie.originalTitle,
//                                movie.overview,
//                                movie.popularity,
//                                movie.posterPath,
//                                movie.releaseDate,
//                                movie.title,
//                                movie.video,
//                                movie.voteAverage,
//                                movie.voteCount
//                            )
//                            dataMovieList.add(newDataMovie)
//                        }
//                        MovieRecyclerView
//                        val adapter=MovieAdapter(dataMovieList)
//                        MovieRecyclerView.adapter=adapter
//
//                    }
//                } else {
//                }
//            }
//
//            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
//                TODO("Not yet implemented")
//            }
//
//        })
//    }
}