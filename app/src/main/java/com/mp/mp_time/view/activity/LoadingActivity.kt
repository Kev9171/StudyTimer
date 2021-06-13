package com.mp.mp_time.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import com.mp.mp_time.R

class LoadingActivity : AppCompatActivity() {

    private val SPLASH_TIME : Long = 5000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)

        val imageView = findViewById<ImageView>(R.id.imageView)
        val textView = findViewById<TextView>(R.id.textView2)

        val animation = AnimationUtils.loadAnimation(this, R.anim.bounce)
        val animation2 = AnimationUtils.loadAnimation(this, R.anim.up)

        imageView.startAnimation(animation)
        textView.startAnimation(animation2)

        Handler().postDelayed({
            startActivity(Intent(this, MainActivity::class.java))

            finish()
        }, SPLASH_TIME)
    }
}