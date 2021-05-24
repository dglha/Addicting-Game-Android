package com.dlha.addictinggame.ui.activities

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.dlha.addictinggame.adapter.GameHavingAdapter
import com.dlha.addictinggame.databinding.ActivityGameHavingBinding
import com.dlha.addictinggame.utils.NetworkResult
import com.dlha.addictinggame.viewmodels.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GameHavingActivity : AppCompatActivity() {

    private var _binding: ActivityGameHavingBinding? = null
    private val binding get() = _binding!!

    private val userViewModel : UserViewModel by viewModels()

    private val mAdapter by lazy { GameHavingAdapter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityGameHavingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.havingToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        setupRecyclerView()
        readApi()
    }

    private fun setupRecyclerView() {
        binding.havingRecyclerView.adapter = mAdapter
        binding.havingRecyclerView.layoutManager = LinearLayoutManager(this)
        showShimmer()
    }

    private fun showShimmer() {
        binding.havingRecyclerView.showShimmer()
    }

    private fun hideShimmer() {
        binding.havingRecyclerView.hideShimmer()
    }

    private fun readApi(){
        lifecycleScope.launch {
            userViewModel.getLibrary()
            userViewModel.gameLibrary.observe(this@GameHavingActivity) { response ->
                when (response) {
                    is NetworkResult.Loading -> {
                        showShimmer()
                    }
                    is NetworkResult.Error -> {
                        Toast.makeText(
                            this@GameHavingActivity,
                            response.message.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                        mAdapter.setData(mutableListOf())
                        hideShimmer()
                    }
                    is NetworkResult.Success -> {
                        response.data?.let {
                            mAdapter.setData(it)
                            hideShimmer()
                        }
                    }
                }
            }
        }
    }
}