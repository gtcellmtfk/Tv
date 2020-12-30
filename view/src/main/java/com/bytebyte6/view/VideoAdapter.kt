package com.bytebyte6.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bytebyte6.base.BaseAdapter
import com.bytebyte6.data.model.IpTv
import com.bytebyte6.data.model.IpTvDiff
import com.bytebyte6.view.databinding.ItemVideoBinding

class VideoAdapter : BaseAdapter<IpTv, VideoViewHolder>(IpTvDiff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder =
        VideoViewHolder.create(parent)

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.binding.apply {
            val item = getItem(position)
            tvName.text = item.name
            Glide.with(ivPreview)
                .load(item.logo)
                .placeholder(R.drawable.ic_landscape)
                .into(ivPreview)
        }
    }
}

class VideoViewHolder(val binding: ItemVideoBinding) : RecyclerView.ViewHolder(binding.root) {
    companion object {
        fun create(parent: ViewGroup): VideoViewHolder {
            return VideoViewHolder(
                ItemVideoBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }
}
