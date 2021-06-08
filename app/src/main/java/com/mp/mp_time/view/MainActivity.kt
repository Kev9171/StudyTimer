package com.mp.mp_time.view

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mp.mp_time.R
import com.mp.mp_time.databinding.ActivityMainBinding
import com.mp.mp_time.viewmodel.FragmentRequest
import com.mp.mp_time.viewmodel.StudyViewModel
import java.util.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    var fragment: Fragment? = null
    val viewModel: StudyViewModel by viewModels()
    lateinit var broadcastReceiver: BroadcastReceiver
    var backkeypress:Long = 0

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(broadcastReceiver)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()
        initViewModel()
        initReceiver()
        initNewDay()

        binding.bottomMenu.selectedItemId = R.id.studyMenu
        supportFragmentManager.beginTransaction()
            .replace(R.id.mainContainer, CalendarFragment())
            .commit()
    }

    private fun initNewDay() {
        // 사용자가 새로운 날짜에 앱을 실행하고 있는: 경우, 그 날에 대한 공부 기록 (과목) 을 생성
        val date = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE) // 오늘 날짜. 'yyyy-mm-dd' 형식
        Log.d("MainActivity", date)
        viewModel.insertNewDate(date)
    }

    private fun initReceiver() {
        // Intent.ACTION_DATE_CHANGED 는 날짜가 바뀔 때마다 전달됨 (즉, PM 11:59 을 지날 때)
        val intentFilter = IntentFilter(Intent.ACTION_DATE_CHANGED) //Intent.ACTION_TIME_TICK (1분마다)
        broadcastReceiver = object: BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                // 날짜가 변경되었으니 해당 날짜에 대한 공부 기록 (과목) 을 생성
                val date = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE) // 오늘 날짜. 'yyyy-mm-dd' 형식
                viewModel.insertNewDate(date)
                //Log.d("MainActivity", date)
            }
        }
        registerReceiver(broadcastReceiver, intentFilter)
    }

    private fun initViewModel() {
        viewModel.fragmentRequest.observe(this) {
            fragment = when(it){
                FragmentRequest.REQUEST_SUBJECT -> AddSubjectFragment()
                FragmentRequest.REQUEST_TIMER -> TimerFragment()
                FragmentRequest.REQUEST_MODIFY -> ModifySubjectFragment()
                FragmentRequest.REQUEST_STUDY -> StudyFragment()
                FragmentRequest.REQUEST_PLACE -> AddPlaceFragment()
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
                R.id.Menu -> MyPlaceFragment()
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

    override fun onResume() {
        super.onResume()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        val str = intent!!.getStringExtra("time")
        if(str == "123")
        {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.mainContainer, TimerFragment())
                    .commit()
        }
    }

    override fun onBackPressed() {


        val fragment = supportFragmentManager.findFragmentById(R.id.mainContainer)

        if(fragment is TimerFragment) {

            if(System.currentTimeMillis() - backkeypress >= 2000) {
                backkeypress = System.currentTimeMillis()
                Toast.makeText(this, "한번 더 클릭시 타이머 종료", Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(this, "타이머 종료", Toast.LENGTH_SHORT).show()
                viewModel.recreate = false
                viewModel.backpressact = true
                viewModel.fragmentTranslationRequest(FragmentRequest.REQUEST_STUDY)

            }

        }
        else
            super.onBackPressed()

    }


}