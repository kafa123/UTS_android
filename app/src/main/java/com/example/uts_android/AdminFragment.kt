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

import com.bumptech.glide.Glide
import com.example.uts_android.Adapter.GenreListAdapter
import com.example.uts_android.databinding.FragmentAdminBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage


class AdminFragment : Fragment() {

    private lateinit var firebase: FirebaseFirestore
    private lateinit var fireStorage: FirebaseStorage
    private lateinit var binding: FragmentAdminBinding
    private lateinit var adapter:GenreListAdapter
    private lateinit var genreList:ArrayList<String>
    private var movieData:Movies=Movies()
    private var ImageUri: Uri? = null
    private var isImageSelected:Boolean=false
    private var doc:String =""
    private lateinit var resultLauncher:ActivityResultLauncher<Intent>



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAdminBinding.inflate(inflater, container, false)
        firebase = FirebaseFirestore.getInstance()
        fireStorage = FirebaseStorage.getInstance()

        genreList= arrayListOf(
            "action","adventure","comedy","drama","horror","thriller","sci-fi"
        )
        adapter=GenreListAdapter(requireContext(),genreList)



        binding.genreCheckbox.adapter=adapter



        resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK && result.data != null) {
                ImageUri = result.data?.data
                isImageSelected=true
                Glide.with(requireContext()).load(ImageUri).into(binding.ivImage)
            }
        }

        binding.buttonImage.setOnClickListener {
                openGallery()
            }
        binding.btnSubmit.setOnClickListener {
            if (isImageSelected==true){
                uploadImage(ImageUri!!)
            }else if(isImageSelected==false && movieData!=null){
                val data = Movies(
                    title = binding.inputTitleMovie.text.toString(),
                    director = binding.inputDirector.text.toString(),
                    description = binding.inputDescription.text.toString(),
                    genres = adapter.getSelectedGenres(),
                    image = movieData.image
                )
                updateData(data)
            }else{
                Toast.makeText(requireContext(), "please input image", Toast.LENGTH_SHORT).show()
            }
        }
        return binding.root
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        val movie=arguments?.getSerializable("Movie")!! as Movies
//
//        with(binding) {
//            if (movieData != null) {
//                inputTitleMovie.setText(movieData.title)
//                inputDescription.setText(movieData.description)
//                inputDirector.setText(movieData.director)
//                doc = movieData.title
//                Glide.with(requireContext()).load(movieData.image).centerCrop().into(ivImage)
//                adapter.setSelectedGenres(movieData.genres)
//
//            }
//        }
//    }

    private fun uploadImage(imageUri: Uri) {
        val storageRef = fireStorage.reference.child("images/${binding.inputTitleMovie.text}")
        val uploadTask = storageRef.putFile(imageUri)
        uploadTask.addOnSuccessListener { taskSnapshot ->
            storageRef.downloadUrl.addOnSuccessListener { uri ->
                val imageUrl = uri
                val Data =Movies(
                    title = binding.inputTitleMovie.text.toString(),
                    director = binding.inputDirector.text.toString(),
                    image = imageUrl.toString(),
                    description = binding.inputDescription.toString(),
                    genres = adapter.getSelectedGenres()
                )
                uploadData(Data)
            }
        }.addOnFailureListener { exception ->
            Log.e("UploadFailure", "Upload failed: $exception")
        }.addOnProgressListener { taskSnapshot ->
            val progress = (100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount)
            Toast.makeText(requireContext(), progress.toString(), Toast.LENGTH_SHORT).show()
        }
    }
    fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        resultLauncher.launch(intent)
    }

    fun uploadData(movie: Movies) {
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
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    fun updateData(movie: Movies){
        if (doc!=movie.title){
            firebase.collection("movies").document(doc).delete()
            uploadData(movie)
        }
        val updates= hashMapOf<String, Any>(
            "director" to movie.director,
            "image" to movie.image,
            "genres" to movie.genres,
            "description" to movie.description,
            "title" to movie.title
        )
        val movieTitle=binding.inputTitleMovie.text.toString()
        firebase.collection("movies").document(movieTitle)
            .update(updates)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Upload successful", Toast.LENGTH_SHORT).show()
                // Move to another page or perform any action upon successful upload
                AdminActivity.viewpagers.currentItem = 1
                clear()
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    fun clear(){
        Glide.with(requireContext()).load(R.drawable.kim).centerCrop().into(binding.ivImage)
        binding.inputTitleMovie.text?.clear()
        binding.inputDirector.text?.clear()
        binding.inputDescription.text?.clear()
        adapter.clear()
    }


}
