package com.bytebyte6.view

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bytebyte6.base_ui.BaseAdapter
import com.bytebyte6.data.model.Image
import com.bytebyte6.view.databinding.ItemImageBinding

enum class ButtonType {
    FAVORITE, DOWNLOAD, NONE
}

class ImageAdapter(
    private val type: ButtonType = ButtonType.NONE,
    private val btnClickListener: ((pos: Int) -> Unit)? = null
) :
    BaseAdapter<Image, ImageViewHolder>(ImageDIFF) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder =
        ImageViewHolder.create(parent)

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        //重建后的recyclerview Item是没有transName的 所以在onbind要重新赋值一遍 动画效果才会有~~
        holder.itemView.transitionName = currentList[position].transitionName
        holder.binding.apply {
            val item = getItem(position)
            tvName.text = item.name

            if (item.logo.isEmpty()) {
                ivPreview.setImageResource(R.drawable.landscape)
            } else {
                GlideApp.with(ivPreview)
                    .load(item.logo)
                    .placeholder(R.drawable.landscape)
                    .into(ivPreview)
            }

            when (type) {
                ButtonType.FAVORITE -> {
                    btnClickListener?.apply {
                        button.visibility = View.VISIBLE
                        if (item.favorite) {
                            button.setImageResource(R.drawable.ic_favorite)
                        } else {
                            button.setImageResource(R.drawable.ic_favorite_border)
                        }
                        button.setOnClickListener {
                            this.invoke(position)
                        }
                    }
                }
                ButtonType.DOWNLOAD -> {
                    btnClickListener?.apply {
                        button.isVisible = !item.download
                        button.setImageResource(R.drawable.ic_download)
                        button.setOnClickListener {
                            this.invoke(position)
                        }
                    }
                }
                ButtonType.NONE -> {
                    button.visibility = View.GONE
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

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(
        oldItem: Image,
        newItem: Image
    ): Boolean {
        return oldItem == newItem
    }
}

