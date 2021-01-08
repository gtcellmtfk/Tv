package com.bytebyte6.base

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bytebyte6.base.databinding.ItemBaseImageBinding

class ImageAdapter : BaseAdapter<ContentProvider, ImageViewHolder>(DIFF) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder =
        ImageViewHolder.create(parent)

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.itemView.transitionName=currentList[position].title
        holder.binding.apply {
            val item = getItem(position)
            tvName.text = item.title
            if (item.url.isEmpty()) {
                return
            }
            Glide.with(ivPreview)
                .load(item.url)
                .placeholder(R.drawable.ic_landscape)
                .into(ivPreview)
        }
    }
}

class ImageViewHolder(val binding: ItemBaseImageBinding) : RecyclerView.ViewHolder(binding.root) {
    companion object {
        fun create(parent: ViewGroup): ImageViewHolder {
            return ImageViewHolder(
                ItemBaseImageBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }
}

object DIFF : DiffUtil.ItemCallback<ContentProvider>() {
    override fun areItemsTheSame(
        oldItem: ContentProvider,
        newItem: ContentProvider
    ): Boolean {
        return oldItem.url == newItem.url
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(
        oldItem: ContentProvider,
        newItem: ContentProvider
    ): Boolean {
        return oldItem == newItem
    }
}

interface ContentProvider {
    val url: String
    val title: String
}
