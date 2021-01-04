package com.bytebyte6.view.video

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bytebyte6.base.BaseAdapter
import com.bytebyte6.data.entity.Tv
import com.bytebyte6.data.entity.TvDiff
import com.bytebyte6.view.R
import com.bytebyte6.view.databinding.ItemVideoBinding

class VideoAdapter : BaseAdapter<Tv, VideoViewHolder>(
    TvDiff
) {

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
