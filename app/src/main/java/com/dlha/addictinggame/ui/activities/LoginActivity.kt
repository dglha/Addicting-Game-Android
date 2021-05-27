package com.dlha.addictinggame.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.dlha.addictinggame.R
import com.dlha.addictinggame.databinding.ActivityLoginBinding
import com.dlha.addictinggame.utils.NetworkResult
import com.dlha.addictinggame.utils.hideKeyboard
import com.dlha.addictinggame.viewmodels.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var authViewModel: AuthViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authViewModel = ViewModelProvider(this).get(AuthViewModel::class.java)

       var usernameFromRegister = intent.getStringExtra("username")
        if(!usernameFromRegister.isNullOrEmpty()) {
            binding.usernameTextInputEditText.setText(usernameFromRegister)
        }

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
        authViewModel.userLogin(username, password)
        authViewModel.userLoginResponse.observe(this) { response ->
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
                    val intent = Intent(this, MainActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                    this.finish()
                }
            }
        }
    }
}