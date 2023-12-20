package com.example.uts_android

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.helper.widget.Carousel
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide

import com.example.uts_android.Adapter.MovieAdapter
import com.example.uts_android.Adapter.UserOnClick
import com.example.uts_android.database.Movies
import com.example.uts_android.database.MoviesDao
import com.example.uts_android.database.MoviesDatabase
import com.example.uts_android.databinding.FragmentHomeBinding

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */



class HomeFragment : Fragment(),UserOnClick
{
    // Adding elements to the titleList
    companion object {
        private var _titleList: ArrayList<String>? = null

        var titleList: ArrayList<String>
            get() {
                if (_titleList == null) {
                    _titleList = arrayListOf()
                }
                return _titleList!!
            }
            set(value) {
                _titleList = value
            }
    }


    private lateinit var firestore: FirebaseFirestore
    private lateinit var firebaseStorage: FirebaseStorage
    private lateinit var binding: FragmentHomeBinding
    private val movieList: MutableLiveData<List<Movies>> by lazy {
        MutableLiveData<List<Movies>>()
    }
    private lateinit var uid:String
    private lateinit var MovieDao: MoviesDao
    private lateinit var executorService: ExecutorService



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        firestore= FirebaseFirestore.getInstance()
        firebaseStorage= FirebaseStorage.getInstance()
        val user=context?.getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE)
        uid=user?.getString("uid",null).toString()
        executorService= Executors.newSingleThreadExecutor()
        val db= MoviesDatabase.getDatabase(requireContext())
        MovieDao=db!!.moviesDao()!!
        getTitleList(uid)

        binding.carousel.setAdapter(object : Carousel.Adapter {
            override fun count(): Int {

                return 5
            }

            override fun populate(view: View, index: Int) {
                when (index) {
                    0 -> Glide.with(view).load(R.drawable.kim).centerCrop().into(view as ImageView)
                    1 -> Glide.with(view).load(R.drawable.ballerina_1).centerCrop().into(view as ImageView)
                    2 -> Glide.with(view).load(R.drawable.doraemon_stand).centerCrop().into(view as ImageView)
                    3 -> Glide.with(view).load(R.drawable.one_piece).centerCrop().into(view as ImageView)
                    4 -> Glide.with(view).load(R.drawable.one_piece_z).centerCrop().into(view as ImageView)
                }
            }

            override fun onNewItem(index: Int) {
                // Called when an item is set
            }
        })


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeMovie()
        observeMoviesChanges()
    }
    private fun observeMovie(){
        movieList.observe(requireActivity()){ movies ->
            val adapter=MovieAdapter(movies,this,titleList){
                val intent= Intent(context,DetailMovies::class.java)
                intent.putExtra("Movies",it)
                startActivity(intent)
            }
            binding.topMoviesRecyclerView.adapter=adapter

            binding.topMoviesRecyclerView.addItemDecoration(DividerItemDecoration(binding.topMoviesRecyclerView.context,
                LinearLayoutManager.VERTICAL))
        }

    }

    private fun observeMoviesChanges() {
        firestore.collection("movies").addSnapshotListener{ snapshots, error ->
            if(error != null){
                Log.d("MainAdminActivity", "Error listening for movies changes: ", error)
                return@addSnapshotListener
            }

            val movies = snapshots?.toObjects(Movies::class.java)
            if (movies != null) {
                movieList.postValue(movies)
            }
        }
    }



    override fun addBookmark(movies: Movies) {
        if (titleList.contains(movies.title)){
            titleList.remove(movies.title)
            val data= hashMapOf<String,Any>(
                "titleList" to titleList
            )
            firestore.collection("Bookmarks").document(uid).set(data)
            firestore.collection("movies").document(movies.title).update("popularity",movies.popularity-1)
            delete(movies)
            getTitleList(uid)
        }else{
        titleList.add(movies.title)
        val data= hashMapOf<String,Any>(
            "titleList" to titleList
        )
        firestore.collection("Bookmarks").document(uid).set(data)
        firestore.collection("movies").document(movies.title).update("popularity",movies.popularity+1)
            insert(movies)
        getTitleList(uid)}
    }
    private fun getTitleList(uid:String){
        firestore.collection("Bookmarks").document(uid).get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                val data = documentSnapshot.data
                if (data != null) {
                    titleList = data["titleList"] as ArrayList<String>
                }
            } else {
                Log.d("HomeFragment", "Document for UID: $uid does not exist")
            }
        }.addOnFailureListener { e ->
            Log.e("HomeFragment", "Error getting document for UID: $uid, $e")
        }
    }

    private fun insert(movies: Movies){
        executorService.execute{MovieDao.insert(movies)}
    }
    private fun delete(movies: Movies){
        executorService.execute{MovieDao.delete(movies)}
    }

}