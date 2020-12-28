package com.bytebyte6.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bytebyte6.base.BaseAdapter
import com.bytebyte6.view.databinding.ItemLoadingBinding

class LoadingAdapter :
    BaseAdapter<Boolean, LoadingViewHolder>(object : DiffUtil.ItemCallback<Boolean>() {
        override fun areItemsTheSame(oldItem: Boolean, newItem: Boolean): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: Boolean, newItem: Boolean): Boolean =
            oldItem == newItem
    }) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LoadingViewHolder =
        LoadingViewHolder.create(parent)

    override fun onBindViewHolder(holder: LoadingViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.itemView.visibility = if (getItem(position)) View.VISIBLE else View.GONE
    }
}

class LoadingViewHolder(val binding: ItemLoadingBinding) : RecyclerView.ViewHolder(binding.root) {
    companion object {
        fun create(parent: ViewGroup): LoadingViewHolder {
            return LoadingViewHolder(
                ItemLoadingBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }
}
