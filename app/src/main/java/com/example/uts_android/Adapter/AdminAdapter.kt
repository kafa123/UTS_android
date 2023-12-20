package com.example.uts_android.Adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bumptech.glide.manager.Lifecycle
import com.example.uts_android.AdminFragment
import com.example.uts_android.MovieAdminFragment

class AdminAdapter(ac: AppCompatActivity): FragmentStateAdapter(ac) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment?= null
        when (position) {
            0 -> fragment = AdminFragment()
            1 -> fragment = MovieAdminFragment()

        }
        return fragment as Fragment
    }
}