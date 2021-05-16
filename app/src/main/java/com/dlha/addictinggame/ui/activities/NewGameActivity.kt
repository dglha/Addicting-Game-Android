package com.dlha.addictinggame.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import com.dlha.addictinggame.R
import com.dlha.addictinggame.adapter.NewGameAdapter
import com.dlha.addictinggame.databinding.ActivityNewGameBinding

class NewGameActivity : AppCompatActivity() {
    private var _binding : ActivityNewGameBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityNewGameBinding.inflate(layoutInflater)

        setSupportActionBar(binding.newGameToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val newgameRecyclerView = binding.newGameRecyclerView
        newgameRecyclerView.adapter = NewGameAdapter(this)



        setContentView(binding.root)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.optionsmenu, menu)
        return true
    }
}