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
import kotlin.math.roundToInt

class SearchAdapter(val context: Context) : RecyclerView.Adapter<SearchAdapter.SearchViewHolder>()
{
    var games: List<GameItem> = emptyList()

    inner class SearchViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        fun bind(game: GameItem){
            itemView.findViewById<TextView>(R.id.favorite_gameTitle_imageView).text = game.name
            itemView.findViewById<ImageView>(R.id.favorite_gameImage_imageView).load(game.image) {
                crossfade(600)
            }
            itemView.findViewById<TextView>(R.id.favorite_gameDeveloper_textView).text = game.developer
            itemView.findViewById<TextView>(R.id.favorite_gameCoin_textView).text = game.coin

            if(game.salePercent.toInt()>0) {
                game.newCoin = ((game.coin.toFloat()*(100-game.salePercent.toFloat()))/100).roundToInt()
            }

            itemView.setOnClickListener {
                val intent = Intent(context, DetailsActivity::class.java).putExtra("item", game)
                Log.d("NavToDetails", "game selected: $game")
                try{
                    context.startActivity(intent)
                } catch (e: Exception){
                    Log.d("NavToDetails", "error when navigate from Favo: " + e.message.toString())
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context).inflate(R.layout.favorite_game_row_layout ,parent,false)
        layoutInflater.setOnClickListener{
            context.startActivity(Intent(context, DetailsActivity::class.java))
        }
        return SearchViewHolder(layoutInflater)
    }

    override fun getItemCount(): Int {
        return games.size
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val currentGame = games[position]
        return holder.bind(currentGame)
    }

    fun setData(newData: List<GameItem>){
        games = newData
        Log.d("TO","OK")
        notifyDataSetChanged()
    }
}