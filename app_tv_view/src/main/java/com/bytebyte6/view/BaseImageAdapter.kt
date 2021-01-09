package com.bytebyte6.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bytebyte6.base_ui.BaseAdapter
import com.bytebyte6.base_ui.R
import com.bytebyte6.base_ui.databinding.ItemBaseImageBinding
import com.bytebyte6.data.model.Image

class ImageAdapter : BaseAdapter<Image, ImageViewHolder>(
    ImageDIFF
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder =
        ImageViewHolder.create(parent)

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.itemView.transitionName = currentList[position].title
        holder.binding.apply {
            val item = getItem(position)
            tvName.text = item.title
            if (item.url.isEmpty()) {
                ivPreview.setImageResource(R.drawable.ic_landscape)
                return
            }
            Glide.with(ivPreview)
                .load(item.url)
                .placeholder(R.drawable.ic_landscape)
                .into(ivPreview)
        }
    }
}

class ImageViewHolder(val binding: ItemBaseImageBinding) :
    RecyclerView.ViewHolder(binding.root) {
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

object ImageDIFF : DiffUtil.ItemCallback<Image>() {
    override fun areItemsTheSame(
        oldItem: Image,
        newItem: Image
    ): Boolean {
        return oldItem.url == newItem.url
    }

    override fun areContentsTheSame(
        oldItem: Image,
        newItem: Image
    ): Boolean {
        return oldItem.url == newItem.url && oldItem.title == newItem.title
    }
}

