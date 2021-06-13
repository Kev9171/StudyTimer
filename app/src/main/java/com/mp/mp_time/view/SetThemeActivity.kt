package com.mp.mp_time.view

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.get
import androidx.fragment.app.findFragment
import androidx.viewpager2.widget.ViewPager2
import com.mp.mp_time.R
import com.mp.mp_time.adapter.ThemeViewPagerAdapter
import com.mp.mp_time.database.MySharedPreferences
import com.mp.mp_time.databinding.ActivitySetThemeBinding
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator

class SetThemeActivity : AppCompatActivity() {

    private val MIN_SCALE = 0.9f // 뷰가 몇퍼센트로 줄어들 것인지
    private val MIN_ALPHA = 0.7f // 어두워지는 정도를 나타낸 듯 하다.

    lateinit var binding: ActivitySetThemeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySetThemeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViewPager()
        init()
        setTheme()

    }

    override fun onPause() {
        super.onPause()
        overridePendingTransition(0,0)
    }



    private fun init(){
        binding.backText.setOnClickListener {
            finishAffinity()
            val intent = Intent(this,MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
        }
    }

    private fun setTheme(){
        binding.setButton.setOnClickListener {
            //val i = intent
            //i.putExtra("set", 설정된값)
            val prefs = MySharedPreferences(applicationContext)
            val k = binding.viewPagerTheme.currentItem
            prefs.set(k.toString())

            val set_theme = prefs?.get().toString().toInt()

            when(set_theme){
                0->{
                    theme.applyStyle(R.style.Theme_MPTIME_light_pink, true)
                }
                1->{
                    theme.applyStyle(R.style.Theme_MPTIME_light_green, true)
                }
                2->{
                    theme.applyStyle(R.style.Theme_MPTIME_dark_blue, true)
                }
                3->{

                }
                4->{
                    theme.applyStyle(R.style.Theme_MPTIME_dark_red, true)
                }
            }

            finishAffinity()
            val intent = Intent(this,MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
            Toast.makeText(this,"테마가 변경되었습니다.",Toast.LENGTH_SHORT).show()
        }
    }


    private fun initViewPager() {

        binding.viewPagerTheme.adapter = ThemeViewPagerAdapter(getThemeList()) // 어댑터 생성
        binding.viewPagerTheme.orientation = ViewPager2.ORIENTATION_HORIZONTAL // 방향을 가로로
        binding.viewPagerTheme.setPageTransformer(ZoomOutPageTransformer()) // 애니메이션 적용


        val dotsIndicator = findViewById<DotsIndicator>(R.id.dots_indicator)
        dotsIndicator.setViewPager2(binding.viewPagerTheme)


    }


    private fun getThemeList(): ArrayList<Int> {
        return arrayListOf<Int>(R.drawable.theme1, R.drawable.theme2, R.drawable.theme3, R.drawable.theme4, R.drawable.theme5)
    }

    inner class ZoomOutPageTransformer : ViewPager2.PageTransformer {
        override fun transformPage(view: View, position: Float) {
            val viewPager = findViewById<ViewPager2>(R.id.viewPager_theme)
            if(viewPager.currentItem==0){
                binding.themeText.text = "WHITE PINK"
            }else if(viewPager.currentItem==1){
                binding.themeText.text = "WHITE GREEN"
            }else if(viewPager.currentItem==2){
                binding.themeText.text = "BLACK BLUE"
            }else if(viewPager.currentItem==3){
                binding.themeText.text = "BLACK GOLD"
            }else if(viewPager.currentItem==4){
                binding.themeText.text = "BLACK RED"
            }else{
                binding.themeText.text = "그 외"
            }

            view.apply {
                val pageWidth = width
                val pageHeight = height
                when {
                    position < -1 -> { // [-Infinity,-1)
                        // This page is way off-screen to the left.
                        alpha = 0f
                    }
                    position <= 1 -> { // [-1,1]
                        // Modify the default slide transition to shrink the page as well
                        val scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position))
                        val vertMargin = pageHeight * (1 - scaleFactor) / 2
                        val horzMargin = pageWidth * (1 - scaleFactor) / 2
                        translationX = if (position < 0) {
                            horzMargin - vertMargin / 2
                        } else {
                            horzMargin + vertMargin / 2
                        }

                        // Scale the page down (between MIN_SCALE and 1)
                        scaleX = scaleFactor
                        scaleY = scaleFactor

                        // Fade the page relative to its size.
                        alpha = (MIN_ALPHA +
                                (((scaleFactor - MIN_SCALE) / (1 - MIN_SCALE)) * (1 - MIN_ALPHA)))
                    }
                    else -> { // (1,+Infinity]
                        // This page is way off-screen to the right.
                        alpha = 0f
                    }
                }
            }
        }
    }
}