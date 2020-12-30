package com.bytebyte6.base

import android.view.View
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<T,V:RecyclerView.ViewHolder>(diffUtil: DiffUtil.ItemCallback<T>) :
    ListAdapter<T,V>(diffUtil) {

    private var onItemClick: ((pos:Int, view:View) -> Unit)? = null

    fun setOnItemClick(onItemClick: ((pos:Int, view:View) -> Unit)) {
        this.onItemClick = onItemClick
    }

    override fun onBindViewHolder(holder: V, position: Int) {
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(position,it)
        }
    }
}
