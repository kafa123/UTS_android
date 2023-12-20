package com.example.uts_android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.example.uts_android.databinding.ActivityBinding
import com.google.android.material.tabs.TabLayoutMediator


class LoginRegister : AppCompatActivity() {
    companion object
    {
        private val TAB_TITLES= intArrayOf(
            R.string.Register,
            R.string.Login
        )
    }

    private lateinit var viewBinding: ActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        val user=getSharedPreferences("user",AppCompatActivity.MODE_PRIVATE)
        val role=user.getString("role",null)

        if (role!=null){
            if (role.equals("Admin") ) {
                val intent = Intent(this, AdminActivity::class.java)
                startActivity(intent)
            } else {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }

        with(viewBinding) {
            val sectionPager = LoginRegisterAdapater(this@LoginRegister)
            val viewPager: ViewPager2 = findViewById(R.id.Fragment)
            viewPager.adapter = sectionPager
            TabLayoutMediator(TabLayout, viewPager) { tab, position ->
                tab.text = resources.getString((TAB_TITLES[position]))
            }.attach()

        }
    }
}