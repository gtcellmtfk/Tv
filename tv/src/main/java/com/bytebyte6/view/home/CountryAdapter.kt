package com.bytebyte6.view.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bytebyte6.common.GlideClearHelper
import com.bytebyte6.common.ImageClearHelper
import com.bytebyte6.data.entity.Country
import com.bytebyte6.data.entity.CountryDiff
import com.bytebyte6.utils.BaseListAdapter
import com.bytebyte6.view.R
import com.bytebyte6.view.adapter.ImageViewHolder
import com.bytebyte6.view.databinding.ItemImageBinding
import com.bytebyte6.view.load


class CountryAdapter(
    private val clearHelper: ImageClearHelper = GlideClearHelper()
) : BaseListAdapter<Country, ImageViewHolder>(CountryDiff), ImageClearHelper by clearHelper {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder =
        ImageViewHolder.create(parent)

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.itemView.transitionName = currentList[position].name
        val item = getItem(position)
        val tvName = holder.binding.tvName
        val ivPreview = holder.binding.ivPreview
        images.add(ivPreview)
        tvName.text = item.name
        if (item.image.isEmpty()) {
            ivPreview.setImageResource(R.drawable.landscape)
        } else {
            ivPreview.load(item.image)
        }
    }
}