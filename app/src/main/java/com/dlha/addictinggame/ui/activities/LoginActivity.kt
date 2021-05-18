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
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import com.dlha.addictinggame.R
import com.dlha.addictinggame.api.ApiClient
import com.dlha.addictinggame.api.AuthService
import com.dlha.addictinggame.data.UserPreferences
import com.dlha.addictinggame.databinding.ActivityLoginBinding
import com.dlha.addictinggame.model.User
import com.dlha.addictinggame.utils.NetworkResult
import com.dlha.addictinggame.utils.hideKeyboard
import com.dlha.addictinggame.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var mainViewModel: MainViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        binding.loginButton.setOnClickListener {
            val username = binding.usernameTextInputEditText.text.toString()
            val password = binding.passwordTextInputEditText.text.toString()
            if (username.isNotEmpty() && password.isNotEmpty()) {
                binding.loginProgressBar.visibility = View.VISIBLE
                hideKeyboard(this)
                login(username, password)
            } else {
                hideKeyboard(this)
                Toast.makeText(
                    this,
                    "Không được để trống Tài Khoản hoặc Mật Khẩu",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }


        binding.createTextView.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            overridePendingTransition(R.anim.slide_in_right, R.anim.stay)
        }

//        changeStatusBarColor()

        val username: String? = intent.getStringExtra("username")
        if (!username.isNullOrEmpty()) {
            binding.usernameTextInputEditText.setText(username.toString())
        }
    }

    //    private fun changeStatusBarColor() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            val window: Window = window
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
//            window.statusBarColor = resources.getColor(R.color.login_bk_color)
//        }
//    }
    private fun login(username: String, password: String) {
        mainViewModel.userLogin(username, password)
        mainViewModel.userLoginResponse.observe(this) { response ->
            when (response) {
                is NetworkResult.Error -> {
                    binding.loginProgressBar.visibility = View.INVISIBLE
                    Toast.makeText(this, response.message.toString(), Toast.LENGTH_SHORT).show()
                }
                is NetworkResult.Loading -> {
                    binding.loginProgressBar.visibility = View.VISIBLE
                }
                is NetworkResult.Success -> {
                    binding.loginProgressBar.visibility = View.INVISIBLE
                    Log.d("LoginActivity", "Success -> token: ${response.data!!.token}")
                    startActivity(Intent(this, MainActivity::class.java))
                }
            }
        }
    }
}