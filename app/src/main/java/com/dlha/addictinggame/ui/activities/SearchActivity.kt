package com.dlha.addictinggame.ui.activities

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.dlha.addictinggame.adapter.SearchAdapter
import com.dlha.addictinggame.databinding.ActivitySearchBinding
import com.dlha.addictinggame.utils.NetworkResult
import com.dlha.addictinggame.utils.hideKeyboard
import com.dlha.addictinggame.viewmodels.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySearchBinding



    private  var token : String = ""

    private val searchViewModel: SearchViewModel by viewModels()

    private val mAdapter: SearchAdapter by lazy { SearchAdapter(this@SearchActivity) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.searchToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        searchViewModel.userToken.observe(this@SearchActivity) {
            token = if(it=="null") {
                ""
            }  else it
        }


        val namegame = intent.getStringExtra("search")
        if(namegame!=null) {
            binding.searchView.setQuery(namegame,false)
            setupRecyclerView()
            readAPI(token,namegame)
        }


        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    setupRecyclerView()
                    readAPI(token,query)
                    hideKeyboard(this@SearchActivity)
                }
                Log.d("TEST",query.toString())
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Log.d("Text change",newText.toString())
                return true
            }
        })


    }
    private fun readAPI(token : String,namegame : String) {
        lifecycleScope.launch {
            searchViewModel.searchGame(token,namegame)
            searchViewModel.listSearchGameResponse.observe(this@SearchActivity) { response ->
                when(response) {
                    is NetworkResult.Loading -> {
                        showShimmerEffect()
                    }
                    is NetworkResult.Error -> {
                        hideShimmerEffect()
                        Toast.makeText(this@SearchActivity,response.message,Toast.LENGTH_SHORT).show()
                    }
                    is NetworkResult.Success -> {
                        response.data?.let { mAdapter.setData(it) }
                        hideShimmerEffect()
                    }
                }

            }
        }
    }
    private fun setupRecyclerView() {
        binding.searchRecyclerView.adapter = mAdapter
        binding.searchRecyclerView.layoutManager = LinearLayoutManager(this)
        showShimmerEffect()
    }
    private fun showShimmerEffect() {
        binding.searchRecyclerView.showShimmer()
    }

    private fun hideShimmerEffect() {
        binding.searchRecyclerView.hideShimmer()
    }

    override fun onSupportNavigateUp(): Boolean {
        this.finish()
        return super.onSupportNavigateUp()
    }
}