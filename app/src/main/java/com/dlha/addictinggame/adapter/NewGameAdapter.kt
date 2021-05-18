package com.dlha.addictinggame.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.dlha.addictinggame.R
import com.dlha.addictinggame.model.GameItem
import com.dlha.addictinggame.ui.activities.DetailsActivity

class NewGameAdapter(val context: Context) : RecyclerView.Adapter<NewGameAdapter.NewGameViewHolder>()
{

    var games: List<GameItem> = emptyList()

    inner class NewGameViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        fun bind(game: GameItem){
            itemView.findViewById<TextView>(R.id.new_gameTitle_textView).text = game.name
            itemView.findViewById<ImageView>(R.id.new_gameImage_imageView).load(game.image){
                crossfade(600)
            }
            itemView.findViewById<TextView>(R.id.new_gameDeveloper_textView).text = game.developer
            itemView.findViewById<TextView>(R.id.new_gameCoin_textView).text = game.coin
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewGameViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context).inflate(R.layout.new_game_row_layout ,parent,false)
        layoutInflater.setOnClickListener{
            context.startActivity(Intent(context, DetailsActivity::class.java))
        }
        return NewGameViewHolder(layoutInflater)
    }

    override fun getItemCount(): Int {
        return games.size
    }

    override fun onBindViewHolder(holder: NewGameViewHolder, position: Int) {
        val currentGame = games[position]
        return holder.bind(currentGame)
    }

    fun setData(newData: List<GameItem>){
        games = newData
        notifyDataSetChanged()
    }
}