package com.example.uts_android.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.uts_android.R
import com.example.uts_android.database.Movies

import com.example.uts_android.databinding.MovieCardBinding

typealias onClickItemMovies = (Movies)->Unit

class MovieAdapter (private var MovieList:List<Movies>,private val userOnClick: UserOnClick,private val titleList:ArrayList<String>, private val OnClickMovie: onClickItemMovies ): RecyclerView.Adapter<MovieAdapter.MovieViewHolder>(){


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
                director.text=data.director
                Glide.with(itemView.context).load(data.image).centerCrop().into(imageMovie)
                itemView.setOnClickListener {
                    OnClickMovie(data)
                }
                addBookmarks.setOnClickListener {
                    userOnClick.addBookmark(data)
                }
                if (titleList.contains(data.title)){
                    addBookmarks.setImageResource(R.drawable.baseline_bookmark_add_24)
                }
            }
        }
    }
}