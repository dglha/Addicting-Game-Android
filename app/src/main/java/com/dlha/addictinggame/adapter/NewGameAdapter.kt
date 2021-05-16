package com.dlha.addictinggame.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.recyclerview.widget.RecyclerView
import com.dlha.addictinggame.R
import com.dlha.addictinggame.ui.activities.DetailsActivity

class NewGameAdapter(val context: Context) : RecyclerView.Adapter<NewGameAdapter.NewGameViewHolder>()
{
    inner class NewGameViewHolder(view : View) : RecyclerView.ViewHolder(view) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewGameViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context).inflate(R.layout.new_game_row_layout,parent,false)
        layoutInflater.setOnClickListener{
            context.startActivity(Intent(context, DetailsActivity::class.java))
        }
        return NewGameViewHolder(layoutInflater)
    }

    override fun getItemCount(): Int {
        return 10
    }

    override fun onBindViewHolder(holder: NewGameViewHolder, position: Int) {

    }
}