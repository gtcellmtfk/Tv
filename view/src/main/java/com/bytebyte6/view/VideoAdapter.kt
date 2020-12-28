package com.bytebyte6.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
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
            tvName.text = getItem(position).name
//            Glide.with(ivPreview)
//                .load(VideoFragment.url)
//                .into(ivPreview)
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
