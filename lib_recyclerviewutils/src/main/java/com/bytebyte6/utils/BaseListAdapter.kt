package com.bytebyte6.utils

import android.view.View
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

abstract class BaseListAdapter<T, V : RecyclerView.ViewHolder>(diffUtil: DiffUtil.ItemCallback<T>) :
    ListAdapter<T, V>(diffUtil) {

    var onItemClick: ((pos: Int, view: View) -> Unit)? = null
    var onItemLongClick: ((pos: Int, view: View) -> Boolean)? = null
    var onCurrentListChanged: ((previous: MutableList<T>, current: MutableList<T>) -> Unit)? = null
    var onBind: ((pos: Int, view: View) -> Unit)? = null

    override fun onBindViewHolder(holder: V, position: Int) {
        onBind?.invoke(position, holder.itemView)
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(position, holder.itemView)
        }
        holder.itemView.setOnLongClickListener {
            onItemLongClick?.invoke(position, holder.itemView)
            false
        }
    }

    override fun onCurrentListChanged(previousList: MutableList<T>, currentList: MutableList<T>) {
        onCurrentListChanged?.invoke(previousList, currentList)
    }
}
