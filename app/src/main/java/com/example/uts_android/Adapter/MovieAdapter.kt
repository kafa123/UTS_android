package com.example.uts_android.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.uts_android.Movies

import com.example.uts_android.databinding.MovieCardBinding

typealias onClickItemMovies = (Movies)->Unit

class MovieAdapter (private var MovieList:List<Movies>, private val OnClickMovie: onClickItemMovies ): RecyclerView.Adapter<MovieAdapter.MovieViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieAdapter.MovieViewHolder {
        val binding= MovieCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return MovieList.size
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(MovieList[position])
    }


    inner class MovieViewHolder(private val binding : MovieCardBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(data: Movies){
            with(binding){
                titleMovie.text=data.title
                itemView.setOnClickListener {
                    OnClickMovie(data)
                }
            }
        }
    }
}