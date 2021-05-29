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
import com.google.android.material.button.MaterialButton
import kotlin.math.roundToInt

class SaleGameModuleAdapter(val context : Context) : RecyclerView.Adapter<SaleGameModuleAdapter.SaleGameModuleViewHolder>() {

    var games : List<GameItem> = emptyList()

    inner class SaleGameModuleViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        fun bind(game : GameItem) {
            itemView.findViewById<TextView>(R.id.module_developer_textView).text  = game.developer
            itemView.findViewById<TextView>(R.id.module_sale_textView).text = "-"+game.salePercent+"%"
            itemView.findViewById<ImageView>(R.id.module_gameImage_imageView).load(game.image) {
                crossfade(600)
            }
            itemView.findViewById<TextView>(R.id.module_gameName_textView).text = game.name
            game.newCoin = ((game.coin.toFloat()*(100-game.salePercent.toFloat()))/100).roundToInt()
            itemView.findViewById<TextView>(R.id.coin_after_sale_textView).text = game.newCoin.toString()
            val spanBuilder = SpannableStringBuilder(game.coin)
            Log.d("TESTING",game.imgtoshow.toString())
            val strikethroughSpan = StrikethroughSpan()
            spanBuilder.setSpan(
                strikethroughSpan,0,game.coin.length,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            itemView.findViewById<TextView>(R.id.module_gameCoin_textView).text = spanBuilder
            if(game.isFavorite > 0) {
                itemView.findViewById<MaterialButton>(R.id.module_addToFavorite_button).visibility = View.GONE
                itemView.findViewById<MaterialButton>(R.id.module_unFavorite_button).visibility = View.VISIBLE
            }

            itemView.setOnClickListener {
                val action = HomeFragmentDirections.actionHomeFragmentToDetailsActivity(game)
                Log.d("NavToDetails", "game selected: $game")
                try{
                    itemView.findNavController().navigate(action)
                } catch (e: Exception){
                    Log.d("NavToDetails", "error when navigate: Sale " + e.message.toString())
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SaleGameModuleAdapter.SaleGameModuleViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context).inflate(R.layout.sale_game_module_row_layout ,parent,false)
        layoutInflater.setOnClickListener{
            context.startActivity(Intent(context, DetailsActivity::class.java))
        }
        return SaleGameModuleViewHolder(layoutInflater)
    }

    override fun getItemCount(): Int {
        return if (games.size > 5) 5 else games.size
    }

    override fun onBindViewHolder(holder: SaleGameModuleAdapter.SaleGameModuleViewHolder, position: Int) {
        val game : GameItem = games[position]
        return holder.bind(game)
    }
    fun setData(newData: List<GameItem>) {
        games = newData
        notifyDataSetChanged()
    }

}