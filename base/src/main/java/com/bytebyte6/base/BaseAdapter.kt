package com.bytebyte6.base

import android.view.View
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<T>(baseDiffUtil: BaseDiffUtil<T>) :
    ListAdapter<T, BaseViewHolder>(baseDiffUtil) {

    private var onLoadMoreListener: OnLoadMoreListener? = null

    fun setOnLoadMoreListener(onLoadMoreListener: OnLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        if (position == itemCount - 1) {
            onLoadMoreListener?.onLoadMore()
        }
    }
}

abstract class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

abstract class BaseDiffUtil<T> : DiffUtil.ItemCallback<T>()

interface OnLoadMoreListener {
    fun onLoadMore()
}