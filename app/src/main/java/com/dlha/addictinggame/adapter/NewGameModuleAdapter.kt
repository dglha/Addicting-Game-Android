package com.dlha.addictinggame.adapter

import android.content.Context
import android.content.Intent
import android.util.Base64
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

//            if(game.image.contains("data:image")){
//                Log.d("NewGameModule", "image base64: " + game.image)
////                Glide.with(itemView)
////                    .load("https://res.cloudinary.com/de8cfygk1/image/upload/v1621325856/fuqotbs7bfd4jdm9flrf.png")
////                    .centerCrop()
////                    .placeholder(R.drawable.ic_error_placeholder)
////                    .into(imageView)
//                imageView.load("https://res.cloudinary.com/de8cfygk1/image/upload/v1621325856/fuqotbs7bfd4jdm9flrf.png")
//            } else {
////                Glide.with(itemView)
////                    .load("https://res.cloudinary.com/de8cfygk1/image/upload/v1621325856/fuqotbs7bfd4jdm9flrf.png")
////                    .centerCrop()
////                    .placeholder(R.drawable.ic_error_placeholder)
////                    .into(imageView)
//                imageView.load("https://res.cloudinary.com/de8cfygk1/image/upload/v1621325856/fuqotbs7bfd4jdm9flrf.png")
//            }

            itemView.setOnClickListener {
                val action = HomeFragmentDirections.actionHomeFragmentToDetailsActivity(game)
                Log.d("NavToDetails", "game selected: " + game.toString())
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
    ): NewGameModuleAdapter.NewGameModuleViewHolder {
        val layoutInflater =
            LayoutInflater.from(parent.context).inflate(R.layout.game_module_row_layout, parent, false)
        return NewGameModuleViewHolder(layoutInflater)
    }

    override fun getItemCount(): Int {
        return games.size
    }

    override fun onBindViewHolder(holder: NewGameModuleAdapter.NewGameModuleViewHolder, position: Int) {
        holder.bind(games[position])
    }

    fun setData(newData: List<GameItem>) {
        games = newData
        notifyDataSetChanged()
    }

}