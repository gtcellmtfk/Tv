package com.bytebyte6.base_ui

import android.view.View
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<T, V : RecyclerView.ViewHolder>(diffUtil: DiffUtil.ItemCallback<T>) :
    ListAdapter<T, V>(diffUtil) {

    private var onItemClick: ((pos: Int, view: View) -> Unit)? = null
    private var onCurrentListChanged: ((previousList: MutableList<T>, currentList: MutableList<T>) -> Unit)? =
        null
    private var onBind: ((pos: Int, view: View) -> Unit)? = null

    fun setOnBind(onItemClick: ((pos: Int, view: View) -> Unit)) {
        this.onBind = onItemClick
    }

    fun setOnItemClick(onItemClick: ((pos: Int, view: View) -> Unit)) {
        this.onItemClick = onItemClick
    }

    fun setOnCurrentListChanged(onCurrentListChanged: ((previousList: MutableList<T>, currentList: MutableList<T>) -> Unit)?) {
        this.onCurrentListChanged = onCurrentListChanged
    }

    fun getOnItemClick(): ((Int, View) -> Unit)? {
        return onItemClick
    }

    override fun onBindViewHolder(holder: V, position: Int) {
        onBind?.invoke(position,holder.itemView)
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(position, holder.itemView)
        }
    }

    override fun onCurrentListChanged(previousList: MutableList<T>, currentList: MutableList<T>) {
        onCurrentListChanged?.invoke(previousList, currentList)
    }
}
