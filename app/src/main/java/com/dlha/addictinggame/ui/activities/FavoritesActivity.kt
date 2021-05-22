    package com.dlha.addictinggame.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import com.dlha.addictinggame.R
import com.dlha.addictinggame.adapter.FavoritesAdapter
import com.dlha.addictinggame.databinding.ActivityFavoritesBinding

class FavoritesActivity : AppCompatActivity() {
    private lateinit var binding : ActivityFavoritesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoritesBinding.inflate(layoutInflater)

        setSupportActionBar(binding.favoriteToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.favoritesRecyclerView.adapter = FavoritesAdapter()

        setContentView(binding.root)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.optionsmenu, menu)
        return true
    }


}