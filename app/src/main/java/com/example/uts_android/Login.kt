package com.example.uts_android

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
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
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var auth:FirebaseAuth
    private lateinit var firebase:FirebaseFirestore
    private lateinit var binding:FragmentLoginBinding
    private val channel="login"

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
                            binding.textPassword.error="please check your username and password again"
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
//                        createNotificationChannel()
                        if (role == "Admin") {
                            val intent = Intent(requireContext(), AdminActivity::class.java)
                            Toast.makeText(requireContext(), "Berhasil Login sebagai admin", Toast.LENGTH_SHORT).show()
                            startActivity(intent)
                        } else {
                            val intent = Intent(requireContext(), MainActivity::class.java)
                            Toast.makeText(requireContext(), "Berhasil Login sebagai user", Toast.LENGTH_SHORT).show()
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

    fun createNotificationChannel() {
        val builder = NotificationCompat.Builder(requireContext(), channel)
            .setContentTitle("LOGIN")
            .setSmallIcon(R.drawable.baseline_person_2_24)
            .setContentText("anda telah berhasil login")
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val notifManager = requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as
                NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notifChannel = NotificationChannel(
                channel,
                "Login",
                NotificationManager.IMPORTANCE_DEFAULT
            )

            with(notifManager) {
                createNotificationChannel(notifChannel)
                notify(0, builder.build())
            }
        }
        else {
            notifManager.notify(0, builder.build())
        }
    }
}