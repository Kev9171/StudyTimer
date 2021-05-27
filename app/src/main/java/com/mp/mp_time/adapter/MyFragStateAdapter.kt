package com.mp.mp_time.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.mp.mp_time.view.CalmFragment
import com.mp.mp_time.view.PeaceFragment
import com.mp.mp_time.view.SadFragment

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