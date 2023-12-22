package com.example.uts_android

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ItemTouchHelper
import com.bumptech.glide.Glide
import com.example.uts_android.Adapter.onClickAdmin
import com.example.uts_android.database.Movies

import com.example.uts_android.databinding.FragmentItemBinding

/**
 * [RecyclerView.Adapter] that can display a [PlaceholderItem].
 * TODO: Replace the implementation with code for your data type.
 */
typealias onClickItemMovies = (Movies)->Unit
class MyMovieAdminRecyclerViewAdapter(
    private val values: List<Movies>,
    private val onClickAdmin: onClickAdmin
) : RecyclerView.Adapter<MyMovieAdminRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FragmentItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.TitleMovie.text = item.title
        holder.DirectorMovie.text = item.director
        Glide.with(holder.itemView.context).load(item.image).centerCrop().into(holder.ImageMovie)
        holder.edit.setOnClickListener {
            onClickAdmin.onEditClick(item)
        }
        holder.delete.setOnClickListener {
            onClickAdmin.onDeleteClick(item)
        }


    }
    override fun getItemCount(): Int = values.size
    inner class ViewHolder(binding: FragmentItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val TitleMovie: TextView = binding.titleMovie
        val ImageMovie: ImageView = binding.imageMovie
        val DirectorMovie:TextView = binding.directorMovie
        val edit: ImageView = binding.editIc
        val delete:ImageView = binding.deleteIc

        init {
            edit.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onClickAdmin.onEditClick(values[position])
                }
            }
        }
    }
    fun enableSwipeToDelete(recyclerView: RecyclerView) {
        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition

                onClickAdmin.onDeleteClick(values[position])
            }
        })
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }


}