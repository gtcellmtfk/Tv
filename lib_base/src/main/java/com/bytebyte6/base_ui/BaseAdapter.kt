package com.bytebyte6.base_ui

import android.view.View
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<T, V : RecyclerView.ViewHolder>(diffUtil: DiffUtil.ItemCallback<T>) :
    ListAdapter<T, V>(diffUtil) {

    private var onItemClick: ((pos: Int, view: View) -> Unit)? = null
    private var onItemLongClick: ((pos: Int, view: View) -> Boolean)? = null
    private var onCurrentListChanged: ((previousList: MutableList<T>, currentList: MutableList<T>) -> Unit)? =
        null
    private var doOnBind: ((pos: Int, view: View) -> Unit)? = null

    fun setDoOnBind(onItemClick: ((pos: Int, view: View) -> Unit)) {
        this.doOnBind = onItemClick
    }

    fun setOnItemClick(onItemClick: ((pos: Int, view: View) -> Unit)) {
        this.onItemClick = onItemClick
    }

    fun setOnItemLongClick(onItemLongClick: ((pos: Int, view: View) -> Boolean)) {
        this.onItemLongClick = onItemLongClick
    }

    fun setOnCurrentListChanged(onCurrentListChanged: ((previousList: MutableList<T>, currentList: MutableList<T>) -> Unit)?) {
        this.onCurrentListChanged = onCurrentListChanged
    }

    fun getOnItemClick(): ((Int, View) -> Unit)? {
        return onItemClick
    }

    override fun onBindViewHolder(holder: V, position: Int) {
        doOnBind?.invoke(position, holder.itemView)
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(position, holder.itemView)
        }
        holder.itemView.setOnLongClickListener {
            if (onItemLongClick == null) {
                false
            } else {
                onItemLongClick!!.invoke(position, holder.itemView)
            }
        }
    }

    override fun onCurrentListChanged(previousList: MutableList<T>, currentList: MutableList<T>) {
        onCurrentListChanged?.invoke(previousList, currentList)
    }
}
