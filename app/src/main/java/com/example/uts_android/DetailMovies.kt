package com.example.uts_android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.uts_android.databinding.ActivityDetailMovieBinding
import com.example.uts_android.model.Genre

class DetailMovies : AppCompatActivity() {

    private lateinit var binding: ActivityDetailMovieBinding
    private lateinit var genreList: ArrayList<Genre>
    private lateinit var recyclerGenresFill:RecyclerView
    private lateinit var genreName:ArrayList<String>
    private lateinit var genre_ids:ArrayList<Int>
    private lateinit var genreIdApi:ArrayList<Int>
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding=ActivityDetailMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)
        genreList = arrayListOf<Genre>()
        genreName = arrayListOf<String>()
        genre_ids = ArrayList()




        val title = intent.getStringExtra("Title")
        val desciption = intent.getStringExtra("Description")
        val image = intent.getStringExtra("Image_Movie")
        val genre_id = intent.getIntegerArrayListExtra("Genres")
        genre_ids= ArrayList(genre_id)






        with(binding){
            titleMovie.text = title
            deskripsiMovie.text = desciption

            recyclerGenresFill=recylerGenres
            val genreApi = GenreApi()
            genreApi.fetchGenres(object : GenreApi.GenreCallback {
                override fun onGenresFetched(genres: List<Genre>) {
                    for (id in genre_ids) {
                        for (genre in genres) {
                            if (id == genre.id) {
                                genreName.add(genre.name)
                            }
                        }
                    }
                    val adapter=GenreAdapter(genreName)
                    recyclerGenresFill.adapter=adapter
                }
                override fun onError(error: String) {
                    // Implement how you want to handle errors here
                }
            })


            Glide.with(this@DetailMovies).load("https://image.tmdb.org/t/p/w500"+image).into(imageDetailMovie)

            binding.btnPesanSekarang.setOnClickListener {
                val intent = Intent(this@DetailMovies,Pemesanan::class.java)
                intent.putExtra("Title",title)
                intent.putExtra("Image",image)
                startActivity(intent)
                finish()
            }
        }
    }


}