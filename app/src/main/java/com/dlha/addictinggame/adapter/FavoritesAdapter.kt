package com.dlha.addictinggame.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.dlha.addictinggame.R
import com.dlha.addictinggame.model.GameItem
import com.dlha.addictinggame.ui.activities.DetailsActivity
import com.dlha.addictinggame.viewmodels.FavoriteViewModel
import kotlin.math.roundToInt

class FavoritesAdapter(val context: Context, val favoriteViewModel: FavoriteViewModel, val lifecycleOwner: LifecycleOwner) : RecyclerView.Adapter<FavoritesAdapter.FavoritesViewHolder>()
{
    var games : MutableList<GameItem> = mutableListOf()

    inner class FavoritesViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        fun bind(game: GameItem, position: Int){
            itemView.findViewById<TextView>(R.id.new_gameTitle_textView).text = game.name
            itemView.findViewById<ImageView>(R.id.new_gameImage_imageView).load(game.image) {
                crossfade(600)
            }
            itemView.findViewById<TextView>(R.id.new_gameDeveloper_textView).text = game.developer
            itemView.findViewById<TextView>(R.id.new_gameCoin_textView).text = game.coin
            if(game.salePercent.toInt() > 0) {
                game.newCoin = ((game.coin.toFloat()*(100-game.salePercent.toFloat()))/100).roundToInt()
            }
            itemView.setOnClickListener {
                val intent = Intent(context, DetailsActivity::class.java).putExtra("item", game)
                Log.d("NavToDetails", "game selected: $game")
                try{
                    context.startActivity(intent)
                } catch (e: Exception){
                    Log.d("NavToDetails", "error when navigate from New: " + e.message.toString())
                }
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesAdapter.FavoritesViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context).inflate(R.layout.new_game_row_layout,parent,false)
        return FavoritesViewHolder(layoutInflater)
    }

    override fun getItemCount(): Int {
        return games.size
    }

    override fun onBindViewHolder(holder: FavoritesAdapter.FavoritesViewHolder, position: Int) {
        holder.bind(games[position], position)
    }

    fun setData(newData: List<GameItem>){
        games = newData as MutableList<GameItem>
        notifyDataSetChanged()
    }
}