package com.bytebyte6.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bytebyte6.common.GlideClearHelper
import com.bytebyte6.common.ImageClearHelper
import com.bytebyte6.data.entity.Tv
import com.bytebyte6.data.entity.TvDiff
import com.bytebyte6.utils.BaseListAdapter
import com.bytebyte6.view.BuildConfig
import com.bytebyte6.view.R
import com.bytebyte6.view.databinding.ItemImageBinding
import com.bytebyte6.view.load
import com.google.android.material.checkbox.MaterialCheckBox

interface ButtonClickListener {
    fun onClick(position: Int, tv: Tv)
}

class TvAdapter(
    private var btnClickListener: ButtonClickListener? = null,
    private val clearHelper: ImageClearHelper = GlideClearHelper(),
    private val download: Boolean = false
) : BaseListAdapter<Tv, ImageViewHolder>(TvDiff), ImageClearHelper by clearHelper {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ImageViewHolder.create(parent)

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        // 重建后的recyclerview Item是没有transName的
        // 所以在onBind要重新赋值一遍 动画效果才会有~~
        holder.itemView.transitionName = currentList[position].tvId.toString()
        val item = getItem(position)
        val tvName = holder.binding.tvName
        val tvPos = holder.binding.tvPosition
        val ivPreview = holder.binding.ivPreview
        val flButton = holder.binding.flButton
        val button: MaterialCheckBox = holder.binding.button
        images.add(ivPreview)
        tvName.text = item.name

        if (BuildConfig.DEBUG) {
            tvPos.text = position.toString()
        } else {
            tvPos.visibility = View.GONE
        }

        ivPreview.load(item.logo)

        flButton.setOnClickListener {
            btnClickListener?.onClick(position, item)
        }

        if (download) {
            if (item.liveContent) {
                flButton.isVisible = false
            } else {
                flButton.isVisible = true
                button.setButtonDrawable(R.drawable.checkbox_download)
            }
        }

        button.isChecked = item.favorite
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

