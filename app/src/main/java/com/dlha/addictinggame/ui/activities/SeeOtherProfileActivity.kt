package com.dlha.addictinggame.ui.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import coil.load
import com.dlha.addictinggame.databinding.ActivitySeeOtherProfileBinding
import com.dlha.addictinggame.model.User
import com.dlha.addictinggame.utils.NetworkResult
import com.dlha.addictinggame.viewmodels.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SeeOtherProfileActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySeeOtherProfileBinding
    private lateinit var profileViewModel: ProfileViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySeeOtherProfileBinding.inflate(layoutInflater)

        profileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)

        setSupportActionBar(binding.otherProfileToolbar)
        supportActionBar?.hide()

        val username = intent.getStringExtra("username")

        if (username != null) {
            Log.d("USERNAME",username)
            readUserInfo(username)
        }

        setContentView(binding.root)
    }

    private fun readUserInfo(username : String) {
        lifecycleScope.launch {
            profileViewModel.getOtherUserInfo(username)
            profileViewModel.otherUserInfoResponse.observe(this@SeeOtherProfileActivity) {response ->
                when(response) {
                    is NetworkResult.Loading -> {
                        Log.d("USERNAME","123")
                    }
                    is NetworkResult.Error -> {
                        Log.d("USERNAME","321")
                    }
                    is NetworkResult.Success -> {
                        setupView(response.data!!)
                        Log.d("USERNAME", response.data.toString())
                    }
                }
            }
        }
    }

    private fun setupView(user : User) {
        binding.usernameTextView.text = user.firstname + " " + user.lastname
        binding.emailTextView.text = user.email
        binding.avatar.load(user.avatar)
    }

}