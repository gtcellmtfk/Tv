package com.bytebyte6.view

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bytebyte6.base.BaseAdapter
import com.bytebyte6.data.model.Category
import com.bytebyte6.data.model.Country
import com.bytebyte6.data.model.Languages


class SingleLineAdapter :
    BaseAdapter<Any, SingleLineItemViewHolder>(AnyDiff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SingleLineItemViewHolder {
        return SingleLineItemViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: SingleLineItemViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        when (val item: Any = getItem(position)) {
            is Country -> {
                holder.text.text = item.countryName
            }
            is Category -> {
                if (item.category.isEmpty()) {
                    val s = holder.text.context.getString(R.string.home_other_category)
                    holder.text.text = s
                } else {
                    holder.text.text = item.category
                }
            }
            is Languages -> {
                val title = item.getString()
                if (title.isEmpty()) {
                    val s = holder.text.context.getString(R.string.home_other_language)
                    holder.text.text = s
                } else {
                    holder.text.text = title
                }
            }
        }
        holder.itemView.transitionName = holder.text.text.toString()
    }
}

object AnyDiff : DiffUtil.ItemCallback<Any>() {
    override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean = oldItem == oldItem

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean = oldItem == oldItem
}

class SingleLineItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val text: TextView = itemView.findViewById(android.R.id.text1)

    companion object {
        fun create(parent: ViewGroup): SingleLineItemViewHolder {
            return SingleLineItemViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    android.R.layout.simple_selectable_list_item,
                    parent,
                    false
                )
            )
        }
    }
}
