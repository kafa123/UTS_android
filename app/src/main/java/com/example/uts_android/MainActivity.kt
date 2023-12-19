package com.example.uts_android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.uts_android.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
private lateinit var Binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(Binding.root)




        with(Binding){
            ReplaceFragment(HomeFragment())
            navbar.setOnItemSelectedListener {
                when (it.itemId) {
                    R.id.nav_home -> ReplaceFragment(HomeFragment())
                    R.id.Profile -> ReplaceFragment(ProfileFragment())
                    else->{}
                }
                true
            }
        }
    }
    private fun ReplaceFragment(Fragment:Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.Frame_layout,Fragment)
        transaction.commit()
    }
}