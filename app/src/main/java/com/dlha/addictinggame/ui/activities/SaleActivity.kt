package com.dlha.addictinggame.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.LinearLayout
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.dlha.addictinggame.R
import com.dlha.addictinggame.adapter.NewGameAdapter
import com.dlha.addictinggame.adapter.SaleGameAdapter
import com.dlha.addictinggame.databinding.ActivitySaleBinding
import com.dlha.addictinggame.utils.NetworkResult
import com.dlha.addictinggame.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SaleActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySaleBinding

    private lateinit var mainViewModel: MainViewModel

    private val mAdapter by lazy { SaleGameAdapter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySaleBinding.inflate(layoutInflater)



        //ViewModel
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        //Setup action bar
        setSupportActionBar(binding.saleToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setupRecyclerView()

        mainViewModel.userToken.observe(this) {
            if(it=="null") {
                readAPI("")
            } else {
                readAPI(it)
            }
        }

        setContentView(binding.root)
    }
    private fun readAPI(token : String) {
        lifecycleScope.launch {
            mainViewModel.getSaleGames(token)
            mainViewModel.saleGameResponse.observe(this@SaleActivity) { response ->
                when(response) {
                    is NetworkResult.Success -> {
                        hideShimmerEffect()
                        response?.data?.let { mAdapter.setData(it) }
                    }
                    is NetworkResult.Loading -> {
                        showShimmerEffect()
                    }
                    is NetworkResult.Error -> {
                        hideShimmerEffect()
                        Toast.makeText(this@SaleActivity, response.message.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
    private fun setupRecyclerView() {
        binding.saleRecyclerView.adapter = mAdapter
        binding.saleRecyclerView.layoutManager = LinearLayoutManager(this)
        showShimmerEffect()
    }
    private fun showShimmerEffect() {
        binding.saleRecyclerView.showShimmer()
    }

    private fun hideShimmerEffect() {
        binding.saleRecyclerView.hideShimmer()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.optionsmenu, menu)
        return true
    }
}