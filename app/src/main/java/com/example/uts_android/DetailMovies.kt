package com.example.uts_android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.uts_android.databinding.ActivityDetailMovieBinding

class DetailMovies : AppCompatActivity() {
    private lateinit var binding: ActivityDetailMovieBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityDetailMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val title = intent.getStringExtra("TITLE")
        val desciption = intent.getStringExtra("Description")
        val image = intent.getIntExtra("Image_Movie",0)

        with(binding){
            titleMovie.text = title
            deskripsiMovie.text = desciption
            imageDetailMovie.setImageResource(image)
        }

    }
}