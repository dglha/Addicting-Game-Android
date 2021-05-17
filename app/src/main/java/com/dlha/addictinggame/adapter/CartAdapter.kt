package com.dlha.addictinggame.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dlha.addictinggame.R


class CartAdapter() : RecyclerView.Adapter<CartAdapter.CartViewHolderViewHolder>()
{
    inner class CartViewHolderViewHolder(view : View) : RecyclerView.ViewHolder(view) {

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolderViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context).inflate(R.layout.cart_game_row_layout,parent,false)
        return CartViewHolderViewHolder(layoutInflater)
    }
    override fun getItemCount(): Int {
        return 10
    }
    override fun onBindViewHolder(holder: CartViewHolderViewHolder, position: Int) {

    }
}