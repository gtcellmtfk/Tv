package com.bytebyte6.base

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<T,V:RecyclerView.ViewHolder>(diffUtil: DiffUtil.ItemCallback<T>) :
    ListAdapter<T,V>(diffUtil) {

    private var onLoadMoreListener: (()->Unit)? = null
    private var onItemClick: ((pos:Int) -> Unit)? = null

    fun setOnLoadMoreListener(onLoadMoreListener:  (()->Unit)?) {
        this.onLoadMoreListener = onLoadMoreListener
    }

    fun setOnItemClick(onItemClick: ((pos:Int) -> Unit)) {
        this.onItemClick = onItemClick
    }

    override fun onBindViewHolder(holder: V, position: Int) {
        if (position == itemCount - 1) {
            onLoadMoreListener?.invoke()
        }
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(position)
        }
    }
}
