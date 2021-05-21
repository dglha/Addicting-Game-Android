package com.dlha.addictinggame.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.dlha.addictinggame.R
import com.dlha.addictinggame.model.Category
import com.dlha.addictinggame.ui.activities.CategoryActivity


class CategoryAdapter(val context : Context) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>()
{
    var categories : List<Category> = emptyList()
    inner class CategoryViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        fun bind(category : Category) {
            itemView.findViewById<TextView>(R.id.name_category_TextView).text = category.nameCategory
            itemView.findViewById<ImageView>(R.id.category_imageView).load(category.categoryImage) {
                crossfade(600)
            }
            Log.d("IDDDD",category.id.toString())
            itemView.setOnClickListener {
                val intent = Intent(context,CategoryActivity::class.java)
                    .putExtra("idcategory",category.id)
                    .putExtra("categoryName", category.nameCategory)
                try {
                    context.startActivity(intent)
                } catch (e : Exception) {

                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context).inflate(R.layout.category_row_layout,parent,false)
        return CategoryViewHolder(layoutInflater)
    }

    override fun getItemCount(): Int {
        return categories.count()
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        var category = categories[position]
        return holder.bind(category)
    }

    fun setData(data : List<Category>) {
        categories = data
        notifyDataSetChanged()
    }
}