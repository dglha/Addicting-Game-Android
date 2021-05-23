package com.dlha.addictinggame.ui.activities

import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dlha.addictinggame.R
import com.dlha.addictinggame.adapter.FavoritesAdapter
import com.dlha.addictinggame.databinding.ActivityFavoritesBinding
import com.dlha.addictinggame.utils.NetworkResult
import com.dlha.addictinggame.utils.SwipeToDelete
import com.dlha.addictinggame.viewmodels.FavoriteViewModel
import com.todkars.shimmer.ShimmerRecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoritesActivity : AppCompatActivity() {
    private var _binding: ActivityFavoritesBinding? = null
    private val binding get() = _binding!!

    private lateinit var favoriteViewModel: FavoriteViewModel

    private lateinit var mAdapter: FavoritesAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityFavoritesBinding.inflate(layoutInflater)
        favoriteViewModel = ViewModelProvider(this).get(FavoriteViewModel::class.java)

        mAdapter = FavoritesAdapter(this, favoriteViewModel, this)

        setSupportActionBar(binding.favoriteToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.favoritesRecyclerView.adapter = mAdapter

        setupRecyclerView()
        readApi()

        swipeToDelete(binding.favoritesRecyclerView)

        setContentView(binding.root)
    }

    private fun swipeToDelete(favoritesRecyclerView: ShimmerRecyclerView) {
        val swipeToDeleteCallback = object : SwipeToDelete(){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val deletedItem = mAdapter.games[viewHolder.bindingAdapterPosition]
                // Delete item
                lifecycleScope.launch {
                    favoriteViewModel.unFavoriteGameHaveId(deletedItem.id)
                    favoriteViewModel.userUnFavoriteResponse.observe(
                        this@FavoritesActivity
                    ) { response ->
                        when (response) {
                            is NetworkResult.Loading -> {
                            }
                            is NetworkResult.Error -> {
                                Toast.makeText(
                                    this@FavoritesActivity,
                                    response.message,
                                    Toast.LENGTH_SHORT
                                ).show()

                            }
                            is NetworkResult.Success -> {
                                Toast.makeText(
                                    this@FavoritesActivity,
                                    "Removed ${deletedItem.name}!",
                                    Toast.LENGTH_SHORT
                                ).show()
                //                                    mAdapter.setData(emptyList())
                                favoriteViewModel.getListFavorite()
                            }
                        }
                    }
                }
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(favoritesRecyclerView)
    }

    
    private fun readApi() {
        lifecycleScope.launch {
            favoriteViewModel.getListFavorite()
            favoriteViewModel.userFavoriteResponse.observe(this@FavoritesActivity) { response ->
                when (response) {
                    is NetworkResult.Loading -> {
                        showShimmer()
                    }
                    is NetworkResult.Error -> {
                        Toast.makeText(
                            this@FavoritesActivity,
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

    private fun hideShimmer() {
        binding.favoritesRecyclerView.hideShimmer()
    }

    private fun setupRecyclerView() {
        binding.favoritesRecyclerView.adapter = mAdapter
        binding.favoritesRecyclerView.layoutManager = LinearLayoutManager(this)
        showShimmer()
    }

    private fun showShimmer() {
        binding.favoritesRecyclerView.showShimmer()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.optionsmenu, menu)
        return true
    }


}