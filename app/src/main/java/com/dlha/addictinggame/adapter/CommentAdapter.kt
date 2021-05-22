package com.dlha.addictinggame.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dlha.addictinggame.R
import com.dlha.addictinggame.model.Comment


class CommentAdapter(val context : Context) : RecyclerView.Adapter<CommentAdapter.CommentViewHolder>()
{
    var comments : List<Comment> = emptyList()
    inner class CommentViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        fun bind(comment: Comment) {
            itemView.findViewById<TextView>(R.id.commentName_textView).text = comment.user_comment
            itemView.findViewById<TextView>(R.id.commentBody_textView).text = comment.text_comment
            itemView.findViewById<TextView>(R.id.commentDate_textView).text = comment.time_comment
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context).inflate(R.layout.comment_row_layout,parent,false)
        return CommentViewHolder(layoutInflater)
    }
    override fun getItemCount(): Int {
        return comments.count()
    }
    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        var comment = comments[position]
        return holder.bind(comment)
    }
    fun setData(data : List<Comment>) {
        comments = data
    }
}