package com.dlha.addictinggame.ui.activities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.dlha.addictinggame.R
import com.dlha.addictinggame.api.ApiClient
import com.dlha.addictinggame.api.AuthService
import com.dlha.addictinggame.data.UserPreferences
import com.dlha.addictinggame.model.User
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private lateinit var username : String
    private lateinit var password : String
    private lateinit var editTextUsername : EditText
    private lateinit var editTextPassword : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        changeStatusBarColor()

        editTextUsername = findViewById(R.id.editTextUsername)
        editTextPassword = findViewById(R.id.editTextPassword)
        val username : String? = intent.getStringExtra("username")
        if(!username.isNullOrEmpty()) {
            editTextUsername.setText(username)
        }
    }
    fun onLoginClick(view : View) {

        username  = editTextUsername.text.toString()
        password  = editTextPassword.text.toString()
        if(username.isNotEmpty() && password.isNotEmpty()) {
            login(username,password)
        } else {
            Toast.makeText(this,"Không được để trống Tài Khoản hoặc Mật Khẩu",Toast.LENGTH_SHORT).show()
        }
    }
    fun onClickRegisterView(view : View) {
        startActivity(Intent(this,RegisterActivity::class.java))
        overridePendingTransition(R.anim.slide_in_left,R.anim.stay)
    }
    private fun changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window: Window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = resources.getColor(R.color.login_bk_color)
        }
    }
    private fun login(username : String,password : String) {
        val loginService = ApiClient.getRetrofit().create(AuthService::class.java)
        val call : Call<User> = loginService.userLogin(username,password)
        call.enqueue(object : Callback<User> {
            override fun onFailure(call: Call<User>, t: Throwable) {
                Toast.makeText(this@LoginActivity,"Failed",Toast.LENGTH_SHORT)
            }
            override  fun onResponse(call: Call<User>, response: Response<User>) {
                val token : String? = response.body()?.token
                Log.d("Token",token.toString())
                if(token.isNullOrEmpty()) {
                    Toast.makeText(this@LoginActivity,"Tên đăng nhập hoặc mật khẩu không chính xác",Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@LoginActivity,token.toString(),Toast.LENGTH_SHORT).show()
                    lifecycleScope.launch {
                        UserPreferences(this@LoginActivity).saveAuthToken(token)
                    }
                    val intent = Intent(this@LoginActivity,MainActivity::class.java)
                    intent.putExtra("token",token)
                    startActivity(intent)
                }
            }
        })
    }

}