package com.mp.mp_time.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.mp.mp_time.view.fragment.CalmFragment
import com.mp.mp_time.view.fragment.PeaceFragment
import com.mp.mp_time.view.fragment.SadFragment

class MyFragStateAdapter(fragmentActivity: FragmentActivity):FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0-> CalmFragment()
            1-> PeaceFragment()
            2-> SadFragment()
            else-> CalmFragment()
        }
    }
}