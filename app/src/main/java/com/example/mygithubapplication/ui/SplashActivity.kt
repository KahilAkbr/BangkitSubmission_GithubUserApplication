package com.example.mygithubapplication.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.mygithubapplication.R
import com.example.mygithubapplication.ui.home.MainActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        @Suppress("DEPRECATION")
        Handler().postDelayed({
            val splashIntent = Intent(this@SplashActivity, MainActivity::class.java)
            startActivity(splashIntent)
            finish()
        },2000)
    }
}