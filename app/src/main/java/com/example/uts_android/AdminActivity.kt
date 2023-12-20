 package com.example.uts_android

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.viewpager2.widget.ViewPager2
import com.example.uts_android.Adapter.AdminAdapter
import com.example.uts_android.databinding.ActivityAdminBinding
import com.google.android.material.tabs.TabLayoutMediator


 class AdminActivity : AppCompatActivity() {
     companion object
     {
         private val ADMIN_TAB= intArrayOf(
             R.string.ADD,
             R.string.Movie
         )
         lateinit  var viewpagers:ViewPager2
     }

    private lateinit var appBarConfiguration: AppBarConfiguration
private lateinit var binding: ActivityAdminBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityAdminBinding.inflate(layoutInflater)
     setContentView(binding.root)
        viewpagers = binding.viewPager

        with(binding){

            val SectionPager= AdminAdapter(this@AdminActivity)
            viewPager.adapter=SectionPager
            TabLayoutMediator(TabLayout,viewPager){
                    tab, position ->
                tab.text = resources.getString((ADMIN_TAB[position]))
            }.attach()

            logout.setOnClickListener {
                val user=getSharedPreferences("user",AppCompatActivity.MODE_PRIVATE)
                user.edit().remove("role").commit()
                val intent=Intent(this@AdminActivity,LoginRegister::class.java)
                startActivity(intent)
                finish()
            }
            }
        }


 }

