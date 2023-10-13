package com.example.uts_android

import android.media.tv.TvContract.Programs.Genres
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
    lateinit var Director : Array<String>
    lateinit var genres : List<String>
    lateinit var MovieGenre : Array<ArrayList<String>>

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
        genres= listOf<String>(
            "Action",
            "Adventure",
            "Slice Of Life",
            "Sci-Fi",
            "Thriller",
            "Romance",
            "Supranatural",
            "Horror"
        )

//        MovieGenre= arrayOf(
//            arrayOf(genres[0],genres[4]),
//            arrayOf(genres[2],genres[5],genres[3]),
//            arrayOf(genres[7],genres[6],genres[4]),
//            arrayOf(genres[0],genres[1]),
//            arrayOf(genres[0],genres[1]),
//        )
        MovieGenre= arrayOf(
            arrayListOf("action","Thriller"),
            arrayListOf("action","Thriller"),
            arrayListOf("action","Thriller"),
            arrayListOf("action","Thriller"),
            arrayListOf("action","Thriller"),
        )

        title=resources.getStringArray(R.array.title)
        description=resources.getStringArray(R.array.description)
        Director=resources.getStringArray(R.array.Director)


        MovieRecyclerView=findViewById(R.id.top_movies_recyclerView)

        MovieList= arrayListOf<movies>()
        getUserData()


        with(Binding){

        }
    }
    private fun getUserData(){
        for (i in imageId.indices){

            val movies = movies(imageId[i],title[i],description[i] , MovieGenre[i],Director[i])
            MovieList.add(movies)
        }

        val adapter=MovieAdapter(MovieList)
        MovieRecyclerView.adapter= adapter
    }


}