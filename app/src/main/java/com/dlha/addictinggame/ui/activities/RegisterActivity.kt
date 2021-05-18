package com.dlha.addictinggame.ui.activities


import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.dlha.addictinggame.R
import com.dlha.addictinggame.api.ApiClient
import com.dlha.addictinggame.api.AuthService
import com.dlha.addictinggame.databinding.ActivityRegisterBinding
import com.dlha.addictinggame.model.Message
import com.dlha.addictinggame.utils.NetworkResult
import com.dlha.addictinggame.utils.hideKeyboard
import com.dlha.addictinggame.viewmodels.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {
    private lateinit var binding : ActivityRegisterBinding

    private lateinit var registerViewModel: RegisterViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding  = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        registerViewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)

        binding.registerButton.setOnClickListener {
            val username : String = binding.usernameTextInputEditText.text.toString()
            val password : String = binding.passwordTextInputEditText.text.toString()
            val email  : String = binding.emailTextInputEditText.text.toString()
            if(email.isNotEmpty() && username.isNotEmpty() && password.isNotEmpty()) {
                binding.registerProgressBar.visibility = View.VISIBLE
                register(email,username,password)
                hideKeyboard(this)
            } else {
                hideKeyboard(this)
                Toast.makeText(this,"Nhập đủ 3 trường",Toast.LENGTH_SHORT).show()
            }
        }
        binding.alreadyTextView.setOnClickListener {
            startActivity(Intent(this,LoginActivity::class.java))
            overridePendingTransition(R.anim.slide_in_left,android.R.anim.slide_out_right);
        }

//        changeStatusBarColor()
    }
//    private fun changeStatusBarColor() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            val window: Window = window
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
//            window.statusBarColor = resources.getColor(R.color.register_bk_color)
//        }
//    }

    private fun register(email : String,username : String,password : String) {
        registerViewModel.userRegister(email,username,password)
        registerViewModel.userRegisterResponse.observe(this) { response ->
            when (response) {
                is NetworkResult.Error -> {
                    binding.registerProgressBar.visibility = View.INVISIBLE
                    Toast.makeText(this, response.message.toString(), Toast.LENGTH_SHORT).show()
                }
                is NetworkResult.Loading -> {
                    binding.registerProgressBar.visibility = View.VISIBLE
                }
                is NetworkResult.Success -> {
                    binding.registerProgressBar.visibility = View.INVISIBLE
                    Log.d("RegisterActivity", "Success -> Message: ${response.data!!.message}")
                    startActivity(Intent(this, MainActivity::class.java))
                }
            }
        }
    }
}