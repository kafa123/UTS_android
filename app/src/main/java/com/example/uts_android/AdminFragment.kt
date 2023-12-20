package com.example.uts_android

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider

import com.bumptech.glide.Glide
import com.example.uts_android.Adapter.GenreListAdapter
import com.example.uts_android.database.Movies
import com.example.uts_android.databinding.FragmentAdminBinding
import com.example.uts_android.model.AdminViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage


class AdminFragment : Fragment() {


    private lateinit var firebase: FirebaseFirestore
    private lateinit var fireStorage: FirebaseStorage
    private lateinit var binding:FragmentAdminBinding
    private lateinit var adapter:GenreListAdapter
    private lateinit var genreList:ArrayList<String>
    private lateinit var adminViewModel: AdminViewModel

    private var ImageUri: Uri? = null
    private var isImageSelected:Boolean=false
    private var doc:String =""
    private lateinit var resultLauncher:ActivityResultLauncher<Intent>



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAdminBinding.inflate(inflater, container, false)
        firebase = FirebaseFirestore.getInstance()
        fireStorage = FirebaseStorage.getInstance()
        adminViewModel= ViewModelProvider(requireActivity())[AdminViewModel::class.java]

        genreList = arrayListOf(
            "action", "adventure", "comedy", "drama", "horror", "thriller", "sci-fi"
        )
        adapter = GenreListAdapter(requireContext(), genreList)

            binding.genreCheckbox.adapter = adapter

            resultLauncher =
                registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                    if (result.resultCode == RESULT_OK && result.data != null) {
                        ImageUri = result.data?.data
                        isImageSelected = true
                        Glide.with(requireContext()).load(ImageUri).centerCrop().into(binding.ivImage)
                    }
                }

            binding.buttonImage.setOnClickListener {
                openGallery()
            }
            binding.btnSubmit.setOnClickListener {
                if (isImageSelected) {
                    uploadImage(ImageUri!!)
                } else {
                    Toast.makeText(requireContext(), "please input image", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            return binding.root
        }

    override fun onResume() {
        super.onResume()
        adminViewModel.selectedMovie.observe(viewLifecycleOwner) { movieData ->
            with(binding) {
                if (movieData != null) {
                    inputTitleMovie.setText(movieData.title)
                    inputDescription.setText(movieData.description)
                    inputDirector.setText(movieData.director)
                    doc = movieData.title
                    Glide.with(requireContext()).load(movieData.image).centerCrop().into(ivImage)
                    adapter.setSelectedGenres(movieData.genres)
                }
                btnSubmit.setOnClickListener {
                    if (!isImageSelected && movieData != null) {
                        val data = Movies(
                            title = binding.inputTitleMovie.text.toString(),
                            director = binding.inputDirector.text.toString(),
                            description = binding.inputDescription.text.toString(),
                            genres = adapter.getSelectedGenres(),
                            image = movieData.image,
                            popularity = movieData.popularity
                        )
                        updateData(data)
                    } else {
                        inputTitleMovie.error = "please input image"
                    }
                }
            }
        }
    }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
             arguments?.let {
                 val movieData: Movies? = it.getSerializable("movieArg") as? Movies
                 with(binding) {
                     if (movieData != null) {
                         inputTitleMovie.setText(movieData.title)
                         inputDescription.setText(movieData.description)
                         inputDirector.setText(movieData.director)
                         doc = movieData.title
                         Glide.with(requireContext()).load(movieData.image).centerCrop().into(ivImage)
                         adapter.setSelectedGenres(movieData.genres)
                     }
                     btnSubmit.setOnClickListener {
                         if (!isImageSelected && movieData != null) {
                         val data = Movies(
                             title = binding.inputTitleMovie.text.toString(),
                             director = binding.inputDirector.text.toString(),
                             description = binding.inputDescription.text.toString(),
                             genres = adapter.getSelectedGenres(),
                             image = movieData.image,
                             popularity = movieData.popularity

                         )
                         updateData(data)
                     }else{
                         inputTitleMovie.error="please input image"
                         }
                     }
                 }
             }

        }

        private fun uploadImage(imageUri: Uri) {
            val storageRef = fireStorage.reference.child("images/${binding.inputTitleMovie.text}")
            val uploadTask = storageRef.putFile(imageUri)
            uploadTask.addOnSuccessListener { _ ->
                storageRef.downloadUrl.addOnSuccessListener { uri ->

                    val data = Movies(
                        title = binding.inputTitleMovie.text.toString(),
                        director = binding.inputDirector.text.toString(),
                        image = uri.toString(),
                        description = binding.inputDescription.text.toString(),
                        genres = adapter.getSelectedGenres(),
                        popularity = 0
                    )
                    uploadData(data)
                }
            }.addOnFailureListener { exception ->
                Log.e("UploadFailure", "Upload failed: $exception")
            }.addOnProgressListener { taskSnapshot ->
                val progress = (100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount)
                Toast.makeText(requireContext(), progress.toString(), Toast.LENGTH_SHORT).show()
            }
        }

        private fun openGallery() {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            resultLauncher.launch(intent)
        }

        private fun uploadData(movie: Movies) {
            val movieTitle = binding.inputTitleMovie.text.toString()

            firebase.collection("movies").document(movieTitle)
                .set(movie)
                .addOnSuccessListener {
                    Toast.makeText(requireContext(), "Upload successful", Toast.LENGTH_SHORT).show()
                    // Move to another page or perform any action upon successful upload
                    AdminActivity.viewpagers.currentItem = 1
                    clear()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT)
                        .show()
                }
        }

        private fun updateData(movie: Movies) {
            if (doc != movie.title) {
                firebase.collection("movies").document(doc).delete()
                uploadData(movie)
            }
            val updates = hashMapOf(
                "director" to movie.director,
                "image" to movie.image,
                "genres" to movie.genres,
                "description" to movie.description,
                "title" to movie.title
            )
            val movieTitle = binding.inputTitleMovie.text.toString()
            firebase.collection("movies").document(movieTitle)
                .update(updates)
                .addOnSuccessListener {
                    Toast.makeText(requireContext(), "Upload successful", Toast.LENGTH_SHORT).show()

                    AdminActivity.viewpagers.currentItem = 1
                    clear()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT)
                        .show()
                }
        }

        private fun clear() {
            Glide.with(requireContext()).load(R.drawable.kim).centerCrop().into(binding.ivImage)
            binding.inputTitleMovie.text?.clear()
            binding.inputDirector.text?.clear()
            binding.inputDescription.text?.clear()
            adapter.clear()
        }


}
