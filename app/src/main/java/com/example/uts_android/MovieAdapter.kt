package com.example.uts_android

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.uts_android.model.DataMovie

class MovieAdapter (private var dataMovieList:ArrayList<DataMovie>): RecyclerView.Adapter<MovieAdapter.MovieViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieAdapter.MovieViewHolder {
        val MovieView = LayoutInflater.from(parent.context).inflate(R.layout.movie_card,parent,false)
        return MovieViewHolder(MovieView)
    }

    override fun getItemCount(): Int {
        return dataMovieList.size
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {

        val currentMovie = dataMovieList[position]
//        holder.imageMovie.setImageResource(currentMovie.posterPath.length)
        holder.titleMovie.text = currentMovie.title

        Glide.with(holder.itemView.context).load("https://image.tmdb.org/t/p/w500"+currentMovie.posterPath).centerCrop().into(holder.imageMovie)

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context,DetailMovies::class.java)
            intent.putExtra("Title",currentMovie.title)
            intent.putExtra("Description",currentMovie.overview)
            intent.putExtra("Image_Movie",currentMovie.posterPath)
            intent.putIntegerArrayListExtra("Genres",currentMovie.genreIds)
            context.startActivity(intent)
        }
    }

    
    class MovieViewHolder(MovieView: View):RecyclerView.ViewHolder(MovieView){
        val imageMovie : ImageView = MovieView.findViewById(R.id.image_movie)
        val titleMovie : TextView = MovieView.findViewById(R.id.title_movie)
    }
}