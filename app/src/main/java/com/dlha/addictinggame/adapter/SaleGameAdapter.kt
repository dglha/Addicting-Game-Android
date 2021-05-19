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
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.dlha.addictinggame.R
import com.dlha.addictinggame.model.GameItem
import com.dlha.addictinggame.ui.activities.DetailsActivity
import com.dlha.addictinggame.ui.fragments.home.HomeFragmentDirections
import kotlin.math.roundToInt

class SaleGameAdapter(val context : Context) : RecyclerView.Adapter<SaleGameAdapter.SaleGameViewHolder>() {

    var games: List<GameItem> = emptyList()

    inner class SaleGameViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        fun bind(game : GameItem) {
            itemView.findViewById<TextView>(R.id.sale_gameTitle_textView).text = game.name
            itemView.findViewById<TextView>(R.id.sale_gameDeveloper_textView).text = game.developer
            itemView.findViewById<ImageView>(R.id.sale_gameImage_imageView).load(game.image) {
                crossfade(600)
            }
            val spanBuilder = SpannableStringBuilder(game.coin)
            val strikethroughSpan = StrikethroughSpan()
            spanBuilder.setSpan(
                strikethroughSpan,0,game.coin.length,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            itemView.findViewById<TextView>(R.id.sale_oldGameCoin_textView).text = spanBuilder
            game.newCoin = ((game.coin.toFloat()*(100-game.salePercent.toFloat()))/100).roundToInt()
            itemView.findViewById<TextView>(R.id.sale_newGameCoin_textView).text = game.newCoin.toString()
            itemView.setOnClickListener {
                val action = HomeFragmentDirections.actionHomeFragmentToDetailsActivity(game)
                Log.d("NavToDetails", "game selected: $game")
                try{
                    itemView.findNavController().navigate(action)
                } catch (e: Exception){
                    Log.d("NavToDetails", "error when navigate: " + e.message.toString())
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SaleGameAdapter.SaleGameViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context).inflate(R.layout.sale_game_row_layout ,parent,false)
        layoutInflater.setOnClickListener{
            context.startActivity(Intent(context, DetailsActivity::class.java))
        }
        return SaleGameViewHolder(layoutInflater)
    }

    override fun getItemCount(): Int {
        return games.count()
    }

    override fun onBindViewHolder(holder: SaleGameAdapter.SaleGameViewHolder, position: Int) {
        val game = games[position]
        return holder.bind(game)
    }
    fun setData(newData : List<GameItem>) {
        games = newData
        notifyDataSetChanged()
    }
}