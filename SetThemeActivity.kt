package com.mp.mp_time

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.mp.mp_time.databinding.ActivitySetThemeBinding
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator

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

    }




    private fun init(){
        binding.backText.setOnClickListener {
            finish()
        }
    }

    private fun setTheme(){
        binding.setButton.setOnClickListener {
            //val i = intent
            //i.putExtra("set", 설정된값)
            finish()
        }
    }


    private fun initViewPager() {

        binding.viewPagerTheme.adapter = ThemeViewPagerAdapter(getThemeList()) // 어댑터 생성
        binding.viewPagerTheme.orientation = ViewPager2.ORIENTATION_HORIZONTAL // 방향을 가로로
        binding.viewPagerTheme.setPageTransformer(ZoomOutPageTransformer()) // 애니메이션 적용



        val dotsIndicator = findViewById<DotsIndicator>(R.id.dots_indicator)
        //val viewPager = findViewById<ViewPager2>(R.id.viewPager_theme)
        dotsIndicator.setViewPager2(binding.viewPagerTheme)
    }


    private fun getThemeList(): ArrayList<Int> {
        return arrayListOf<Int>(R.drawable.theme1, R.drawable.theme2)
    }

    inner class ZoomOutPageTransformer : ViewPager2.PageTransformer {
        override fun transformPage(view: View, position: Float) {
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