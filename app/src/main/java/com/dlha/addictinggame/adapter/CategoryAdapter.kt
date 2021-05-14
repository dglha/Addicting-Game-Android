package com.dlha.addictinggame.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.dlha.addictinggame.R


class CategoryAdapter() : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>()
{
    inner class CategoryViewHolder(view : View) : RecyclerView.ViewHolder(view) {
            val categoryImageView: ImageView = view.findViewById(R.id.category_imageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context).inflate(R.layout.category_row_layout,parent,false)
        return CategoryViewHolder(layoutInflater)
    }

    override fun getItemCount(): Int {
        return 10
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.categoryImageView.setImageResource(R.drawable.fd1dda13059050ea67316cdc29198af8)
    }
}