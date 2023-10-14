package com.example.uts_android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.uts_android.databinding.ActivityDetailMovieBinding

class DetailMovies : AppCompatActivity() {

    private lateinit var binding: com.example.uts_android.databinding.ActivityDetailMovieBinding
    private lateinit var genreList: ArrayList<Genre>
    private lateinit var recyclerGenres:RecyclerView
    lateinit var genreArray : ArrayList<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityDetailMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val title = intent.getStringExtra("Title")
        val desciption = intent.getStringExtra("Description")
        val Director = intent.getStringExtra("Director")
        val image = intent.getIntExtra("Image_Movie",0)
        val genres = intent.getStringArrayListExtra("Genres")
        genreArray= genres as ArrayList<String>
        recyclerGenres=findViewById(R.id.recycler_genres)

        genreList= arrayListOf<Genre>()
        getGenreList()

        with(binding){
            titleMovie.text = title
            deskripsiMovie.text = desciption
            Glide.with(this@DetailMovies).load(image).into(imageDetailMovie)
            director.text=Director

            binding.btnPesanSekarang.setOnClickListener {
                val intent = Intent(this@DetailMovies,Pemesanan::class.java)
                intent.putExtra("Title",title)
                intent.putExtra("Image",image)
                startActivity(intent)
                finish()
            }

        }

    }
    private fun getGenreList(){
        for (i in genreArray.indices){
            val genre = Genre(genreArray[i])
            genreList.add(genre)
        }
        recyclerGenres.adapter=GenreAdapter(genreList)
    }
}