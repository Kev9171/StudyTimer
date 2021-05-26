package com.mp.mp_time.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import com.mp.mp_time.R
import com.mp.mp_time.databinding.ActivityMainBinding
import com.mp.mp_time.viewmodel.StudyViewModel

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    var fragment: Fragment? = null
    val viewModel: StudyViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()

        binding.bottomMenu.selectedItemId = R.id.studyMenu
        supportFragmentManager.beginTransaction()
            .replace(R.id.mainContainer, TimerFragment())
            .commit()


    }


    private fun init() {
        binding.apply {
            bottomMenu.setOnNavigationItemSelectedListener {
                fragment = when(it.itemId) {
                    R.id.schedulerMenu -> TimerFragment() // TODO fragment 바꾸기
                    R.id.studyMenu -> TimerFragment()
                    R.id.userMenu -> TimerFragment() // TODO fragment 바꾸기
                    R.id.Menu -> TimerFragment() // TODO 추가기능
                    else -> TimerFragment()
                }
                fragment?.let {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.mainContainer, it)
                        .commit()
                } ?: Log.d("ERROR", "Error:: can't make new fragment")
                return@setOnNavigationItemSelectedListener true
            }
        }
    }
}