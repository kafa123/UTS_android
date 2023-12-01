package com.example.uts_android

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class AdminAdapter(ac: AppCompatActivity): FragmentStateAdapter(ac) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment?= null
        when (position) {
            0 -> fragment = AdminFragment()
            1 -> fragment = HomeFragment()

        }
        return fragment as Fragment
    }
}