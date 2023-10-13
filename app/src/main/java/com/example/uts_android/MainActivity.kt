package com.example.uts_android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.example.uts_android.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var MovieRecyclerView: RecyclerView
    private lateinit var MovieList: ArrayList<movies>
    lateinit var imageId:Array<Int>
    lateinit var title:Array<String>
    lateinit var description:Array<String>
    lateinit var sutradara : Array<String>
    lateinit var genres : Array<List<String>>

    private lateinit var Binding:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(Binding.root)

        imageId= arrayOf(
            R.drawable.ballerina_1,
            R.drawable.doraemon_stand,
            R.drawable.nun_2,
            R.drawable.one_piece,
            R.drawable.one_piece_z,
        )

        title=resources.getStringArray(R.array.title)
        description=resources.getStringArray(R.array.description)
        sutradara=resources.getStringArray(R.array.)

        MovieRecyclerView=findViewById(R.id.top_movies_recyclerView)

        MovieList= arrayListOf<movies>()
        getUserData()

        with(Binding){

        }
    }
    private fun getUserData(){
        for (i in imageId.indices){
            val movies = movies(imageId[i],title[i],description[i] )
            MovieList.add(movies)
        }

        val adapter=MovieAdapter(MovieList)
        MovieRecyclerView.adapter= adapter
    }


}