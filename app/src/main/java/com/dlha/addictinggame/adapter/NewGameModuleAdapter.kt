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

class NewGameModuleAdapter(val context: Context) :
    RecyclerView.Adapter<NewGameModuleAdapter.NewGameModuleViewHolder>() {
    var games: List<GameItem> = emptyList()

    inner class NewGameModuleViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(game: GameItem) {
            itemView.findViewById<TextView>(R.id.module_gameName_textView).text = game.name
            val imageView = itemView.findViewById<ImageView>(R.id.module_gameImage_imageView)
            itemView.findViewById<TextView>(R.id.module_developer_textView).text = game.developer
            itemView.findViewById<TextView>(R.id.module_gameCoin_textView).text = game.coin
            imageView.load(game.image){
                crossfade(600)
            }
            if(game.isFavorite>0) {
                itemView.findViewById<MaterialButton>(R.id.module_addToFavorite_button).setIconResource(R.drawable.ic_heart)
                itemView.findViewById<MaterialButton>(R.id.module_addToFavorite_button).setIconTintResource(R.color.red)
            }
            if(game.salePercent.toInt()>0) {
                game.newCoin = ((game.coin.toFloat()*(100-game.salePercent.toFloat()))/100).roundToInt()
            }
            Log.d("QQQ",game.isFavorite.toString())
            itemView.setOnClickListener {
                val action = HomeFragmentDirections.actionHomeFragmentToDetailsActivity(game)
                Log.d("NavToDetails", "game selected: $game")
                try{
                    itemView.findNavController().navigate(action)
                } catch (e: Exception){
                    Log.d("NavToDetails", "error when navigate: " + e.message.toString())
                    val intent = Intent(context, DetailsActivity::class.java).putExtra("item", game)
                    context.startActivity(intent)
                }

            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NewGameModuleAdapter.NewGameModuleViewHolder {
        val layoutInflater =
            LayoutInflater.from(parent.context).inflate(R.layout.game_module_row_layout, parent, false)
        return NewGameModuleViewHolder(layoutInflater)
    }

    override fun getItemCount(): Int {
        return if (games.size > 5) 5 else games.size
    }

    override fun onBindViewHolder(holder: NewGameModuleAdapter.NewGameModuleViewHolder, position: Int) {
        holder.bind(games[position])
    }

    fun setData(newData: List<GameItem>) {
        games = newData
        notifyDataSetChanged()
    }

}