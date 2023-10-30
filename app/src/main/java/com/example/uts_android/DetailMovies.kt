package com.example.uts_android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.uts_android.databinding.ActivityDetailMovieBinding
import com.example.uts_android.model.Genre

class DetailMovies : AppCompatActivity() {

    private lateinit var binding: ActivityDetailMovieBinding
    private lateinit var genreList: ArrayList<Genre>
    private lateinit var recyclerGenres:RecyclerView
    private lateinit var genreName:ArrayList<String>
    private lateinit var genre_ids:List<Int>
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding=ActivityDetailMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)
        genreList = ArrayList()
        genreName = ArrayList()
        genre_ids = emptyList()


        val title = intent.getStringExtra("Title")
        val desciption = intent.getStringExtra("Description")
        val image = intent.getStringExtra("Image_Movie")
        val genre_id = intent.getIntArrayExtra("Genre")
        if(genre_id!=null){
            genre_ids=genre_id.toList()
        }

        recyclerGenres=findViewById(R.id.recyler_genres)
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
            }
            override fun onError(error: String) {
                // Implement how you want to handle errors here
            }
        })


        with(binding){
            titleMovie.text = title
            deskripsiMovie.text = desciption
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