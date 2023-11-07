package com.example.uts_android

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemLongClickListener
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.uts_android.databinding.MovieCardBinding
import com.example.uts_android.model.DataMovie
typealias OnClickMovie =(DataMovie)->Unit
class MovieAdapter (private var dataMovieList:ArrayList<DataMovie>, private val OnClickMovie:OnClickMovie ): RecyclerView.Adapter<MovieAdapter.MovieViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieAdapter.MovieViewHolder {
        val binding=MovieCardBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MovieViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataMovieList.size
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {

        holder.bind(dataMovieList[position])
    }

    
    inner class MovieViewHolder(private val binding :MovieCardBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(data:DataMovie){
            with(binding){
                titleMovie.text=data.title
                Glide.with(itemView.context).load("https://image.tmdb.org/t/p/w500"+data.posterPath).centerCrop().into(imageMovie)
                rate.text=data.popularity.toString()
                itemView.setOnClickListener {
                    OnClickMovie(data)
                }
            }
        }
    }
}