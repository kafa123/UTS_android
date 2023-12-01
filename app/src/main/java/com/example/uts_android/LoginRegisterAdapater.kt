package com.example.uts_android

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class LoginRegisterAdapater(ac: AppCompatActivity): FragmentStateAdapter(ac) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment?= null
        when (position) {
            0 -> fragment = Register()
            1 -> fragment = Login()

        }
        return fragment as Fragment
    }
}