package com.example.imageshare

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main3.*

class MainActivity3 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)
image_charge.setOnClickListener {
    val intent= Intent(this,MainActivity6::class.java)
    startActivity(intent)
    finish()
        }

    }
}
