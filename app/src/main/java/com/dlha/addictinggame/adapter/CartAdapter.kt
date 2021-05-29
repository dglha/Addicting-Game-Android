package com.dlha.addictinggame.adapter

import android.content.Context
import android.media.Image
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.StrikethroughSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.dlha.addictinggame.R
import com.dlha.addictinggame.model.GameItem
import kotlin.math.roundToInt


class CartAdapter(val context : Context) : RecyclerView.Adapter<CartAdapter.CartViewHolderViewHolder>()
{
    var gamesInCart : List<GameItem> = emptyList()
    inner class CartViewHolderViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        fun bind(gameItem: GameItem) {
            itemView.findViewById<ImageView>(R.id.cart_gameImage_imageView).load(gameItem.image) {
                crossfade(600)
            }
            itemView.findViewById<TextView>(R.id.cart_gameDeveloper_textView).text = gameItem.developer
            itemView.findViewById<TextView>(R.id.cart_gameTitle_imageView).text = gameItem.name
            itemView.findViewById<TextView>(R.id.cart_gameCoin_textView).text = gameItem.coin
            if(gameItem.salePercent.toInt() > 0) {
                gameItem.newCoin = ((gameItem.coin.toFloat()*(100-gameItem.salePercent.toFloat()))/100).roundToInt()
                itemView.findViewById<TextView>(R.id.cart_game_newCoin_textView).visibility = View.VISIBLE
                itemView.findViewById<TextView>(R.id.cart_game_newCoin_textView).text = gameItem.newCoin.toString()

                val spanBuilder = SpannableStringBuilder(gameItem.coin)
                val strikethroughSpan = StrikethroughSpan()
                spanBuilder.setSpan(
                    strikethroughSpan,0,gameItem.coin.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )

                itemView.findViewById<TextView>(R.id.cart_gameCoin_textView).text = spanBuilder

            } else {
                itemView.findViewById<TextView>(R.id.cart_gameCoin_textView).text = gameItem.coin
                itemView.findViewById<TextView>(R.id.cart_game_newCoin_textView).visibility = View.INVISIBLE
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolderViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context).inflate(R.layout.cart_game_row_layout,parent,false)
        return CartViewHolderViewHolder(layoutInflater)
    }
    override fun getItemCount(): Int {
        return gamesInCart.count()
    }
    override fun onBindViewHolder(holder: CartViewHolderViewHolder, position: Int) {
        val game = gamesInCart[position]
        holder.bind(game)
    }
    fun setData(data : List<GameItem>) {
        gamesInCart = data
        notifyDataSetChanged()
    }
}