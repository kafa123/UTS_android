package com.example.uts_android

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.uts_android.databinding.FragmentLoginBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class Login : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var auth:FirebaseAuth
    private lateinit var firebase:FirebaseFirestore
    private lateinit var binding:FragmentLoginBinding

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
        binding= FragmentLoginBinding.inflate(layoutInflater,container,false)
        auth=Firebase.auth
        firebase= FirebaseFirestore.getInstance()
        return binding.root
    }
    override fun onResume() {
        super.onResume()
        with(binding){
            btnLogin.setOnClickListener {
                auth.signInWithEmailAndPassword(textUsername.text.toString()+"@gmail.com",textPassword.text.toString())
                    .addOnCompleteListener(requireActivity()){
                            task->
                        if (task.isSuccessful) {
                            val user = auth.currentUser
                            updateUI(user)

                        }else{
                            Toast.makeText(requireContext(),"eror",Toast.LENGTH_LONG).show()
                        }
                    }
            }
        }
    }
    private fun updateUI(user: FirebaseUser?){
        if (user!=null){
            firebase= FirebaseFirestore.getInstance()
            firebase.collection("users").document(user.uid)
                .get().addOnSuccessListener {
                        document->
                    if(document!=null && document.exists()){
                        val userData=document.data!!
                        val sharePref=context?.getSharedPreferences("user",AppCompatActivity.MODE_PRIVATE)
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


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Login.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Login().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}