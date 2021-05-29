package com.dlha.addictinggame.adapter

import android.content.Context
import android.content.Intent
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.StrikethroughSpan
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


class GameInCategoryAdapter(val context : Context) : RecyclerView.Adapter<GameInCategoryAdapter.GameInCategoryAdapterViewHolder>()
{
    var gamesInCategory : List<GameItem> = emptyList()
    inner class GameInCategoryAdapterViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        fun bind(game: GameItem) {
            itemView.findViewById<TextView>(R.id.sale_gameTitle_textView).text = game.name
            itemView.findViewById<TextView>(R.id.sale_gameDeveloper_textView).text = game.developer
            itemView.findViewById<ImageView>(R.id.sale_gameImage_imageView).load(game.image) {
                crossfade(600)
            }
            val spanBuilder = SpannableStringBuilder(game.coin)
            val strikethroughSpan = StrikethroughSpan()
            spanBuilder.setSpan(
                strikethroughSpan,0,game.coin.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            Log.d("zxc",game.isFavorite.toString())
            if(game.salePercent.toInt()>0) {
                game.newCoin = ((game.coin.toFloat()*(100-game.salePercent.toFloat()))/100).roundToInt()
                itemView.findViewById<TextView>(R.id.sale_newGameCoin_textView).visibility = View.VISIBLE
                itemView.findViewById<TextView>(R.id.sale_oldGameCoin_textView).text = spanBuilder
                itemView.findViewById<TextView>(R.id.sale_newGameCoin_textView).text = game.newCoin.toString()
            } else {
                itemView.findViewById<TextView>(R.id.sale_newGameCoin_textView).visibility = View.GONE
                itemView.findViewById<TextView>(R.id.sale_oldGameCoin_textView).text = game.coin
            }
            itemView.setOnClickListener {
                val intent = Intent(context, DetailsActivity::class.java).putExtra("item", game)
                Log.d("NavToDetails", "game selected: $game")
                try{
                    context.startActivity(intent)
                } catch (e: Exception){
                    Log.d("NavToDetails", "error when navigate from Sale: " + e.message.toString())
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameInCategoryAdapterViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context).inflate(R.layout.sale_game_row_layout,parent,false)
        return GameInCategoryAdapterViewHolder(layoutInflater)
    }

    override fun getItemCount(): Int {
        return gamesInCategory.count()
    }

    override fun onBindViewHolder(holder: GameInCategoryAdapterViewHolder, position: Int) {
        val category = gamesInCategory[position]
        return holder.bind(category)
    }

    fun setData(data : List<GameItem>) {
        gamesInCategory = data
        notifyDataSetChanged()
        Log.d("SUCCESS", "setData: called")
    }
}