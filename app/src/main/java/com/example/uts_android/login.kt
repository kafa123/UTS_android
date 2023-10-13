package com.example.uts_android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.uts_android.databinding.ActivityLoginBinding

class login : AppCompatActivity() {
    companion object{
        const val EXTRA_USERNAME="extra_username"
    }

    private lateinit var viewBinding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding=ActivityLoginBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)


        with(viewBinding){
            viewBinding.btnLogin.setOnClickListener {
                val intent= Intent(this@login,MainActivity::class.java)
                intent.putExtra(EXTRA_USERNAME,textUsername.text.toString())
                startActivity(intent)
            }
        }
    }
}