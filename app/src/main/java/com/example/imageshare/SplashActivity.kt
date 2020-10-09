package com.example.imageshare

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_splash.*


class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.SplashTheme)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_splash)

        val topAnim= AnimationUtils.loadAnimation(this,R.anim.top_animation)
        val bottomAnim= AnimationUtils.loadAnimation(this,R.anim.bottom_animation)

        splash_image.animation=topAnim
        welcome.animation=bottomAnim
        sellby.animation=bottomAnim

        vf3.flipInterval = 3500
        vf3.startFlipping()
        image_trolley.animation=topAnim


        val handler= Handler()
        handler.postDelayed({
          val intent= Intent(this,ShowActivity::class.java)
            startActivity(intent)
            finish()
        },5000)



    }
}
