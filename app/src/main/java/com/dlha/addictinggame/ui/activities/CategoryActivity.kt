package com.dlha.addictinggame.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.navigation.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.dlha.addictinggame.adapter.CategoryAdapter
import com.dlha.addictinggame.adapter.GameInCategoryAdapter
import com.dlha.addictinggame.databinding.ActivityCategoryBinding
import com.dlha.addictinggame.model.GameItem
import com.dlha.addictinggame.utils.NetworkResult
import com.dlha.addictinggame.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CategoryActivity : AppCompatActivity() {
    private lateinit var binding : ActivityCategoryBinding


    private lateinit var mainViewModel: MainViewModel

    private val mAdapter by lazy { GameInCategoryAdapter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryBinding.inflate(layoutInflater)


        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        setupToolbar()

        val idcategory = intent.getIntExtra("idcategory",0)
        Log.d("QQQ",idcategory.toString())

        setupRecyclerView()
        readAPI(idcategory)

        setContentView(binding.root)
    }
    private fun setupToolbar() {
        setSupportActionBar(binding.categoryToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
    private fun readAPI(idcategory : Int) {
        lifecycleScope.launch {
            mainViewModel.getGamesInCategory(idcategory)
            mainViewModel.gamesInCategoryResponse.observe(this@CategoryActivity) { response ->
                when(response) {
                    is NetworkResult.Loading -> {

                        showShimmerEffect()
                    }
                    is NetworkResult.Error -> {
                        hideShimmerEffect()

                        Toast.makeText(this@CategoryActivity,response.message.toString(), Toast.LENGTH_SHORT).show()
                    }
                    is NetworkResult.Success -> {
                        Log.d("SUCCESS","ERR")
                        Log.d("SUCCESS", "readAPI: " + response.data.toString())
                        response.data?.let { mAdapter.setData(it) }
                        hideShimmerEffect()
                    }
                }
            }
        }
    }
    private fun setupRecyclerView() {
        binding.gameInCategoryRecyclerView.adapter = mAdapter
        binding.gameInCategoryRecyclerView.layoutManager = LinearLayoutManager(this)
        showShimmerEffect()
    }
    private fun showShimmerEffect() {
        binding.gameInCategoryRecyclerView.showShimmer()
    }

    private fun hideShimmerEffect() {
        binding.gameInCategoryRecyclerView.hideShimmer()
    }
}