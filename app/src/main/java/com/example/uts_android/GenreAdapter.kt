package com.example.uts_android

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.uts_android.model.Genre

class GenreAdapter(private val genreList:ArrayList<Genre>):RecyclerView.Adapter<GenreAdapter.ViewHolder>() {
    inner class ViewHolder(genreView: View):RecyclerView.ViewHolder(genreView){
        val genreTextView:TextView=genreView.findViewById(R.id.tv_genre)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val genreView = LayoutInflater.from(parent.context).inflate(R.layout.genres,parent,false)
        return ViewHolder(genreView)
    }

    override fun getItemCount() = genreList.size
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.genreTextView.text= genreList[position].name
    }
}