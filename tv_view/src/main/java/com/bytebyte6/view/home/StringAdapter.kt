package com.bytebyte6.view.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bytebyte6.base.BaseAdapter
import com.bytebyte6.view.R

class StringAdapter : BaseAdapter<String, StringViewHolder>(StringDiff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StringViewHolder {
        return StringViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: StringViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val item = getItem(position)
        holder.tvTitle.text= item
        holder.itemView.transitionName = item
    }
}

object StringDiff : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean = oldItem == oldItem

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean = oldItem == oldItem
}

class StringViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)

    companion object {
        fun create(parent: ViewGroup): StringViewHolder {
            return StringViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_string,
                    parent,
                    false
                )
            )
        }
    }
}
