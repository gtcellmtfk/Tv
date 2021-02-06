package com.bytebyte6.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bytebyte6.common.GlideClearHelper
import com.bytebyte6.common.ImageClearHelper
import com.bytebyte6.data.entity.Tv
import com.bytebyte6.data.entity.TvDiff
import com.bytebyte6.utils.BaseListAdapter
import com.bytebyte6.view.R
import com.bytebyte6.view.databinding.ItemImageBinding
import com.bytebyte6.view.load

enum class ButtonType {
    FAVORITE, DOWNLOAD, NONE
}

interface ButtonClickListener {
    fun onClick(position: Int,tv:Tv)
}

class TvAdapter(
    private val type: ButtonType = ButtonType.NONE,
    private var btnClickListener: ButtonClickListener? = null,
    private val clearHelper: ImageClearHelper = GlideClearHelper()
) : BaseListAdapter<Tv, ImageViewHolder>(TvDiff), ImageClearHelper by clearHelper {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder =
        ImageViewHolder.create(parent)

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        //重建后的recyclerview Item是没有transName的 所以在onBind要重新赋值一遍 动画效果才会有~~
        holder.itemView.transitionName = currentList[position].name
        val item = getItem(position)
        val tvName = holder.binding.tvName
        val ivPreview = holder.binding.ivPreview
        val button = holder.binding.button
        images.add(ivPreview)
        tvName.text = item.name

        if (item.logo.isEmpty()) {
            ivPreview.setImageResource(R.drawable.landscape)
        } else {
            ivPreview.load(item.logo)
        }
        button.setOnClickListener {
            btnClickListener?.onClick(position,item)
        }
        when (type) {
            ButtonType.FAVORITE -> {
                button.visibility = View.VISIBLE
                if (item.favorite) {
                    button.setImageResource(R.drawable.ic_favorite)
                } else {
                    button.setImageResource(R.drawable.ic_favorite_border)
                }
            }
            ButtonType.DOWNLOAD -> {
                button.isVisible = !item.download
                button.setImageResource(R.drawable.ic_download)
            }
            ButtonType.NONE -> {
                button.visibility = View.GONE
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

