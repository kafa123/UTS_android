package com.example.uts_android

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.uts_android.databinding.FragmentRegisterBinding
import com.google.android.material.R
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Register.newInstance] factory method to
 * create an instance of this fragment.
 */
class Register : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null


    private lateinit var auth:FirebaseAuth
    private lateinit var firebase:FirebaseFirestore
    private lateinit var role:ArrayList<String>
    private lateinit var binding:FragmentRegisterBinding


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
        binding= FragmentRegisterBinding.inflate(layoutInflater,container,false)
        auth=Firebase.auth
        role= arrayListOf<String>(
            "Admin",
            "User"
        )
        firebase= FirebaseFirestore.getInstance()
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        with(binding){

            val adapter=ArrayAdapter(requireContext(),android.R.layout.simple_spinner_dropdown_item,role)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter=adapter


            btnRegister.setOnClickListener {
                auth.createUserWithEmailAndPassword(textUsername.text.toString()+"@gmail.com", textPassword.text.toString())
                    .addOnCompleteListener(requireActivity()) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success")
                            val user = auth.currentUser
                            user?.let {
                                val userData =HashMap<String,Any>()
                                userData["username"]=textUsername.text.toString()
                                userData["nama lengkap"]=textNameLengkap.text.toString()
                                userData["Role"]=spinner.selectedItem.toString()

                                firebase.collection("users").document(user.uid)
                                    .set(userData)
                                    .addOnCompleteListener {
                                        Toast.makeText(requireContext(),"success",Toast.LENGTH_SHORT).show()
                                    }
                                    .addOnFailureListener { e->
                                        Toast.makeText(requireContext(),e.message,Toast.LENGTH_SHORT).show()
                                    }
                            }
                            updateUI(user)
                            makeBookmark(user)
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.exception)
                            Toast.makeText(
                                requireContext(),
                                task.exception.toString(),
                                Toast.LENGTH_SHORT,
                            ).show()
                            updateUI(null)
                        }
                    }
            }
        }
        return binding.root
    }

    fun updateUI(user:FirebaseUser?){
        if (user!=null){
            firebase= FirebaseFirestore.getInstance()
            firebase.collection("users").document(user.uid)
                .get().addOnSuccessListener {
                    document->
                    if(document!=null && document.exists()){
                        val userData=document.data!!
                        val sharePref=context?.getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE)
                        val role=userData["Role"]as String
                        sharePref?.edit()?.putString("role",role)?.apply()
                        sharePref?.edit()?.putString("uid",user.uid)?.apply()
                        if (role == "Admin") {
                            val intent = Intent(requireContext(), AdminActivity::class.java)
                            startActivity(intent)
                        } else {
                            val intent = Intent(requireContext(), MainActivity::class.java)
                            startActivity(intent)
                        }
                    }
                }
        }
    }
    private fun makeBookmark(user:FirebaseUser?){
        val emptyList= arrayListOf<String>()
        val Data= hashMapOf<String,Any>()
        Data["titleList"]=emptyList
        firebase.collection("Bookmarks").document(user!!.uid).set(
            Data
        )
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Register.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Register().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}


