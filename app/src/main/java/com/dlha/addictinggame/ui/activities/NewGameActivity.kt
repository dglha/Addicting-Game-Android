package com.dlha.addictinggame.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.dlha.addictinggame.R
import com.dlha.addictinggame.adapter.NewGameAdapter
import com.dlha.addictinggame.adapter.NewGameModuleAdapter
import com.dlha.addictinggame.databinding.ActivityNewGameBinding
import com.dlha.addictinggame.utils.NetworkResult
import com.dlha.addictinggame.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NewGameActivity : AppCompatActivity() {
    private var _binding : ActivityNewGameBinding? = null
    private val binding get() = _binding!!

    private lateinit var mainViewModel: MainViewModel

    private val mAdapter by lazy { NewGameAdapter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityNewGameBinding.inflate(layoutInflater)

        //ViewModel
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        //Setup action bar
        setSupportActionBar(binding.newGameToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setupRecycleView()

        mainViewModel.userToken.observe(this) {
            if(it=="null") {
                readApi("")
            } else {
                readApi(it)
            }
        }

        setContentView(binding.root)
    }

    private fun readApi(token : String) {
        lifecycleScope.launch {
            mainViewModel.getNewGames(token)
            mainViewModel.newGamesResponse.observe(this@NewGameActivity) { response ->
                when (response) {
                    is NetworkResult.Success -> {
                        Log.d("NewGameActivity", "readApi: success")
                        hideShimmerEffect()
                        response.data?.let {
                            mAdapter.setData(it)
                        }
                    }
                    is NetworkResult.Error -> {
                        hideShimmerEffect()
                        Toast.makeText(this@NewGameActivity, response.message.toString(), Toast.LENGTH_SHORT).show()
                    }
                    is NetworkResult.Loading -> {
                        showShimmerEffect()
                    }
                }
            }
        }
    }

    private fun setupRecycleView() {
        binding.newGameRecyclerView.adapter = mAdapter
        binding.newGameRecyclerView.layoutManager = LinearLayoutManager(this)
        showShimmerEffect()
    }

    private fun showShimmerEffect() {
        binding.newGameRecyclerView.showShimmer()
    }

    private fun hideShimmerEffect() {
        binding.newGameRecyclerView.hideShimmer()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.optionsmenu, menu)
        return true
    }
}