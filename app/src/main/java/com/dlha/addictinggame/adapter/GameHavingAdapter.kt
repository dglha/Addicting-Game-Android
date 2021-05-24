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
import com.dlha.addictinggame.model.GameItem
import com.dlha.addictinggame.ui.activities.DetailsActivity

class GameHavingAdapter(val context: Context) : RecyclerView.Adapter<GameHavingAdapter.GameHavingViewHolder>()
{
    var games : MutableList<GameItem> = mutableListOf()

    inner class GameHavingViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        fun bind(game: GameItem, position: Int){
            itemView.findViewById<TextView>(R.id.gameHaving_gameTitle_textView).text = game.name
            itemView.findViewById<ImageView>(R.id.gameHaving_gameImage_imageView).load(game.image) {
                crossfade(600)
            }
            itemView.findViewById<TextView>(R.id.gameHaving_gameDeveloper_textView).text = game.developer
            itemView.findViewById<TextView>(R.id.gameHaving_gameCoin_textView).text = game.coin

            itemView.setOnClickListener {
                val intent = Intent(context, DetailsActivity::class.java).putExtra("item", game)
                Log.d("NavToDetails", "game selected: $game")
                try{
                    context.startActivity(intent)
                } catch (e: Exception){
                    Log.d("NavToDetails", "error when navigate from Lib: " + e.message.toString())
                }
            }

        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameHavingViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context).inflate(R.layout.game_having_row_layout,parent,false)
        return GameHavingViewHolder(layoutInflater)
    }

    override fun getItemCount(): Int {
        return games.size
    }

    override fun onBindViewHolder(holder: GameHavingViewHolder, position: Int) {
        holder.bind(games[position], position)
    }

    fun setData(newData: List<GameItem>){
        games = newData as MutableList<GameItem>
        notifyDataSetChanged()
    }
}