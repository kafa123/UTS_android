package com.example.uts_android

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.example.uts_android.Adapter.MovieAdapter
import com.example.uts_android.Adapter.UserOnClick
import com.example.uts_android.database.Movies
import com.example.uts_android.database.MoviesDao
import com.example.uts_android.database.MoviesDatabase
import com.example.uts_android.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment(),UserOnClick {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentProfileBinding
    private lateinit var MovieDao:MoviesDao
    private lateinit var auth:FirebaseAuth
    private lateinit var executorService: ExecutorService
    private lateinit var titleList: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentProfileBinding.inflate(inflater,container,false)
        executorService=Executors.newSingleThreadExecutor()
        val db=MoviesDatabase.getDatabase(requireContext())
        MovieDao=db!!.moviesDao()!!
        val view=binding.root
        titleList= HomeFragment.titleList
        auth= FirebaseAuth.getInstance()

        with(binding){
            logut.setOnClickListener {
                val user=context?.getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE)
                user?.edit()?.remove("role")?.apply()
                val intent=Intent(requireActivity(),LoginRegister::class.java)
                startActivity(intent)
                auth.signOut()
                activity?.finish()
            }

        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getAllBookmarks()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
    private fun getAllBookmarks(){
        MovieDao.allMovies().observe(requireActivity()){movies->
            Log.e("error",movies.toString())
            val adapter=MovieAdapter(movies,this,titleList){
                val intent=Intent(requireActivity(),DetailMovies::class.java)
                intent.putExtra("Movies",it)
                startActivity(intent)
            }
            binding.recycleBookmarks.adapter=adapter
        }
    }

    override fun Bookmark(movies: Movies) {
        delete(movies)
    }
    private fun delete(movies: Movies){
        executorService.execute{MovieDao.delete(movies)}
    }

}