package com.example.uts_android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.uts_android.Adapter.GenreAdapter
import com.example.uts_android.database.Movies
import com.example.uts_android.databinding.ActivityDetailMovieBinding
import com.example.uts_android.model.Genre

class DetailMovies : AppCompatActivity() {

    private lateinit var binding: ActivityDetailMovieBinding

    private lateinit var recyclerGenresFill:RecyclerView
    private lateinit var genre:List<String>
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding=ActivityDetailMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)
        genre= listOf<String>()



        val movies=intent.getSerializableExtra("Movies") as Movies
        genre= movies.genres






        with(binding){
            titleMovie.text = movies.title
            deskripsiMovie.text = movies.description
            recyclerGenresFill=recylerGenres
                    val adapter= GenreAdapter(genre)
                    recyclerGenresFill.adapter=adapter
            director.text=movies.director
            Glide.with(this@DetailMovies).load(movies.image).into(imageDetailMovie)

            binding.btnPesanSekarang.setOnClickListener {
                val intent = Intent(this@DetailMovies,Pemesanan::class.java)
                intent.putExtra("Movies",movies)
                startActivity(intent)
                finish()
            }
        }
    }


}