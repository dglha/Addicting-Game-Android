package com.dlha.addictinggame.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.lifecycle.lifecycleScope
import com.dlha.addictinggame.R
import com.dlha.addictinggame.data.UserPreferences
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Handler().postDelayed({
            isLogin()
        },3000)
    }
    private fun isLogin() {
        lifecycleScope.launch {
            val token = UserPreferences(this@SplashActivity).getAuthToken()
            if(token!=null) {
                startActivity(Intent(this@SplashActivity,MainActivity::class.java).putExtra("token",token))
            } else {
                startActivity(Intent(this@SplashActivity,MainActivity::class.java))
            }
        }
    }
}