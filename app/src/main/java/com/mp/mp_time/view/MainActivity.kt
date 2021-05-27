package com.mp.mp_time.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mp.mp_time.R
import com.mp.mp_time.databinding.ActivityMainBinding
import com.mp.mp_time.viewmodel.FragmentRequest
import com.mp.mp_time.viewmodel.StudyViewModel

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    var fragment: Fragment? = null
    val viewModel: StudyViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()
        initViewModel()

        binding.bottomMenu.selectedItemId = R.id.studyMenu
        supportFragmentManager.beginTransaction()
            .replace(R.id.mainContainer, CalendarFragment())
            .commit()
    }

    private fun initViewModel() {
        viewModel.fragmentRequest.observe(this) {
            fragment = when(it){
                FragmentRequest.REQUEST_SUBJECT -> AddSubjectFragment()
                FragmentRequest.REQUEST_TIMER -> TimerFragment()
            }

            val fragmentTranslation = supportFragmentManager.beginTransaction()
                .replace(R.id.mainContainer, fragment!!)
            fragmentTranslation.addToBackStack(null)
            fragmentTranslation.commit()
        }
    }


    private fun init() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomMenu)
        bottomNavigationView.setOnNavigationItemSelectedListener { item: MenuItem ->
            fragment = when(item.itemId) {
                R.id.schedulerMenu -> CalendarFragment()
                R.id.studyMenu -> StudyFragment()
                R.id.userMenu -> UserSettingsFragment()
                R.id.Menu -> StudyFragment() // TODO 추가기능
                else -> StudyFragment()
            }
            fragment?.let { fragment ->
                supportFragmentManager.beginTransaction()
                    .replace(R.id.mainContainer, fragment)
                    .commit()
            } ?: Log.e("ERROR", "Error:: can't make new fragment")
            true
        }
    }
}