package com.mp.mp_time.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.mp.mp_time.R
import com.mp.mp_time.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    var fragment: Fragment? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()

        binding.bottomMenu.selectedItemId = R.id.studyMenu
        supportFragmentManager.beginTransaction()
            .replace(R.id.mainContainer, StudyFragment())
            .commit()
    }


    private fun init() {
        binding.apply {
            bottomMenu.setOnNavigationItemSelectedListener {
                fragment = when(it.itemId) {
                    R.id.schedulerMenu -> StudyFragment() // TODO fragment 바꾸기
                    R.id.studyMenu -> StudyFragment()
                    R.id.userMenu -> StudyFragment() // TODO fragment 바꾸기
                    R.id.Menu -> StudyFragment() // TODO 추가기능
                    else -> StudyFragment()
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