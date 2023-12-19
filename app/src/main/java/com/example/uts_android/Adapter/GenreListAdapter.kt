package com.example.uts_android.Adapter

import android.content.Context
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.TextView
import com.example.uts_android.R

class GenreListAdapter(private val context: Context, private val genreList: ArrayList<String>) : BaseAdapter() {

    private val checkedItems = SparseBooleanArray()

    override fun getCount(): Int = genreList.size

    override fun getItem(position: Int): Any = genreList[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.genre_checkbox, parent, false)

        val genreTextView = view.findViewById<TextView>(R.id.genre_text_view)
        val genreCheckBox = view.findViewById<CheckBox>(R.id.checkbox)

        genreTextView.text = genreList[position]
        genreCheckBox.isChecked = checkedItems[position]

        genreCheckBox.setOnCheckedChangeListener { _, isChecked ->
            checkedItems.put(position, isChecked)
        }

        return view
    }

    fun getSelectedGenres():ArrayList<String>{
        val items=ArrayList<String>()

        for(i in 0  until genreList.size){
            if(checkedItems.get(i)){
                items.add(genreList[i])
            }
        }
        return items
    }
    fun setSelectedGenres(selectedGenres: List<String>) {
        // Clear previously selected items
        checkedItems.clear()

        // Mark items in checkedItems based on selectedGenres
        for (i in genreList.indices) {
            val genre = genreList[i]
            if (selectedGenres.contains(genre)) {
                checkedItems.put(i, true)
            }
        }

        // Notify adapter data set changed to reflect the changes in checkboxes
        notifyDataSetChanged()
    }
    fun clear(){
        checkedItems.clear()
    }
}
