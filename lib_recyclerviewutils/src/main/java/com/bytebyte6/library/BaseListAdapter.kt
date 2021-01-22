package com.bytebyte6.library

import android.view.View
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

abstract class BaseListAdapter<T, V : RecyclerView.ViewHolder>(diffUtil: DiffUtil.ItemCallback<T>) :
    ListAdapter<T, V>(diffUtil) {

    var onItemClick: ((pos: Int, view: View) -> Unit)? = null
    var onItemLongClick: ((pos: Int, view: View) -> Boolean)? = null
    var onCurrentListChanged: ((previous: MutableList<T>, current: MutableList<T>) -> Unit)? = null
    var doOnBind: ((pos: Int, view: View) -> Unit)? = null

    override fun onBindViewHolder(holder: V, position: Int) {
        holder.itemView.setOnClickListener(null)
        holder.itemView.setOnLongClickListener(null)
        doOnBind?.invoke(position, holder.itemView)
        onItemClick?.apply {
            holder.itemView.setOnClickListener {
                this.invoke(position, holder.itemView)
            }
        }
        onItemLongClick?.apply {
            holder.itemView.setOnLongClickListener {
                this.invoke(position, holder.itemView)
            }
        }
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        onItemClick = null
        onItemLongClick = null
        doOnBind = null
        onCurrentListChanged = null
    }

    override fun onCurrentListChanged(previousList: MutableList<T>, currentList: MutableList<T>) {
        onCurrentListChanged?.invoke(previousList, currentList)
    }
}
