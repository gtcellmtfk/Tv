package com.bytebyte6.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bytebyte6.base.logd
import com.bytebyte6.base_ui.BaseAdapter
import com.bytebyte6.data.model.Image
import com.bytebyte6.view.databinding.ItemImageBinding

class ImageAdapter(
    private val fragment: Fragment,
    private val favClickListener: ((pos: Int) -> Unit)? = null
) :
    BaseAdapter<Image, ImageViewHolder>(ImageDIFF) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder =
        ImageViewHolder.create(parent)

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        //重建后的recyclerview Item是没有transName的 所以在onbind要重新赋值一遍 动画效果才会有~~
        holder.itemView.transitionName = currentList[position].title
        holder.binding.apply {
            val item = getItem(position)
            tvName.text = item.title

            logd(item.title+" "+item.imageUrl)
            if (item.imageUrl.isEmpty()) {
                ivPreview.setImageResource(R.drawable.landscape)
            } else {
                GlideApp.with(ivPreview)
                    .load(item.imageUrl)
                    .placeholder(R.drawable.landscape)
                    .into(ivPreview)
            }

            favClickListener?.apply {
                ivFavorite.visibility = View.VISIBLE
                if (item.love) {
                    ivFavorite.setImageResource(R.drawable.ic_favorite)
                } else {
                    ivFavorite.setImageResource(R.drawable.ic_favorite_border)
                }
                ivFavorite.setOnClickListener {
                    this.invoke(position)
                }
            }
        }
    }
}

class ImageViewHolder(val binding: ItemImageBinding) :
    RecyclerView.ViewHolder(binding.root) {
    companion object {
        fun create(parent: ViewGroup): ImageViewHolder {
            return ImageViewHolder(
                ItemImageBinding.inflate(
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
        return oldItem.videoUrl == newItem.videoUrl
    }

    override fun areContentsTheSame(
        oldItem: Image,
        newItem: Image
    ): Boolean {
        return oldItem.imageUrl == newItem.imageUrl
                && oldItem.title == newItem.title
                && oldItem.love == newItem.love
                && oldItem.videoUrl == newItem.videoUrl
    }
}

