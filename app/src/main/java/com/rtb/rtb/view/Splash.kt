package com.rtb.rtb.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.rtb.rtb.R
import com.rtb.rtb.database.preferences.SharedPrefs

class Splash : AppCompatActivity() {
    private val splashTimeOut: Long = 3000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
    }

    override fun onStart() {
        super.onStart()

        Handler().postDelayed({
            val isLogged = SharedPrefs(this).getUser()
            if(isLogged) {
                val intent = Intent(this, ProjectHome::class.java)
                startActivity(intent)
            } else {
                val intent = Intent(this, SignIn::class.java)
                startActivity(intent)
            }

            finish()
        }, splashTimeOut)
    }
}