package com.example.uts_android

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import com.example.uts_android.Adapter.onClickAdmin
import com.example.uts_android.database.Movies
import com.example.uts_android.databinding.FragmentItemListBinding
import com.example.uts_android.model.AdminViewModel

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

/**
 * A fragment representing a list of Items.
 */
class MovieAdminFragment : Fragment(),onClickAdmin{

    private var columnCount = 1
    private lateinit var binding: FragmentItemListBinding
    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var firebaseStorage: FirebaseStorage
    private val movieList:MutableLiveData<List<Movies>> by lazy {
        MutableLiveData<List<Movies>>()
    }
    private lateinit var adminViewModel: AdminViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding= FragmentItemListBinding.inflate(layoutInflater,container,false)
        firebaseFirestore= FirebaseFirestore.getInstance()
        firebaseStorage= FirebaseStorage.getInstance()
        adminViewModel=ViewModelProvider(requireActivity()).get(AdminViewModel::class.java)
        with(binding){

        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeMovie()
        observeMoviesChanges()
    }
    private fun observeMovie(){
        movieList.observe(requireActivity()){ movies ->
            val adapter = MyMovieAdminRecyclerViewAdapter(movies,this)
            binding.list.adapter = adapter
        }
    }

    private fun observeMoviesChanges() {
        firebaseFirestore.collection("movies").addSnapshotListener{ snapshots, error ->
            // jika dia terjadi error maka dia akan memunculkan log
            if(error != null){
                Log.d("MainAdminActivity", "Error listening for movies changes: ", error)
                return@addSnapshotListener
            }
            // jika gak error maka akan langsung ke sini
            val movies = snapshots?.toObjects(Movies::class.java)
            if (movies != null) {
                movieList.postValue(movies)
            }
        }
    }

    companion object {
        const val ARG_COLUMN_COUNT = "column-count"
        @JvmStatic
        fun newInstance(columnCount: Int) =
            MovieAdminFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }

    override fun onEditClick(movie: Movies) {
        adminViewModel.setSelectedMovie(movie)
        val viewPager=AdminActivity.viewpagers
        viewPager.currentItem=0
    }

    override fun onDeleteClick(movie: Movies) {
        firebaseStorage.reference.child("images/${movie.title}").delete()
        firebaseFirestore.collection("movies").document(movie.title).delete()
    }
}