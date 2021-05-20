package com.dlha.addictinggame.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.dlha.addictinggame.R
import com.dlha.addictinggame.model.Category
import com.dlha.addictinggame.model.GameItem
import com.dlha.addictinggame.ui.activities.CategoryActivity


class GameInCategoryAdapter(val context : Context) : RecyclerView.Adapter<GameInCategoryAdapter.GameInCategoryAdapterViewHolder>()
{
    var gamesInCategory : List<GameItem> = emptyList()
    inner class GameInCategoryAdapterViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        fun bind(gameItem: GameItem) {
            itemView.findViewById<TextView>(R.id.favorite_gameTitle_imageView).text = gameItem.name
            itemView.findViewById<ImageView>(R.id.favorite_gameImage_imageView).load(gameItem.image) {
                crossfade(600)
            }
            itemView.findViewById<TextView>(R.id.favorite_gameTitle_imageView).text = gameItem.developer
            itemView.findViewById<TextView>(R.id.favorite_gameCoin_textView).text = gameItem.coin

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameInCategoryAdapterViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context).inflate(R.layout.favorite_game_row_layout,parent,false)
        return GameInCategoryAdapterViewHolder(layoutInflater)
    }

    override fun getItemCount(): Int {
        return gamesInCategory.count()
    }

    override fun onBindViewHolder(holder: GameInCategoryAdapterViewHolder, position: Int) {
        var category = gamesInCategory[position]
        return holder.bind(category)
    }

    fun setData(data : List<GameItem>) {
        gamesInCategory = data
        notifyDataSetChanged()
        Log.d("SUCCESS", "setData: called")
    }
}