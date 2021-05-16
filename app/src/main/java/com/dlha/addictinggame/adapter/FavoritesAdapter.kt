package com.dlha.addictinggame.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dlha.addictinggame.R

class FavoritesAdapter() : RecyclerView.Adapter<FavoritesAdapter.FavoritesViewHolder>()
{
    inner class FavoritesViewHolder(view : View) : RecyclerView.ViewHolder(view) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context).inflate(R.layout.favorite_game_row_layout,parent,false)
        return FavoritesViewHolder(layoutInflater)
    }

    override fun getItemCount(): Int {
        return 10
    }

    override fun onBindViewHolder(holder: FavoritesViewHolder, position: Int) {

    }
}