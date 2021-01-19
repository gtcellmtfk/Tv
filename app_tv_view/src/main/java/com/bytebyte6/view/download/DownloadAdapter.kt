package com.bytebyte6.view.download

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.selection.Selection
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bytebyte6.base_ui.BaseAdapter
import com.bytebyte6.data.model.TvAndDownload
import com.bytebyte6.view.GlideApp
import com.bytebyte6.view.R
import com.bytebyte6.view.card.Details
import com.bytebyte6.view.card.DetailsLookup
import com.bytebyte6.view.card.KeyProvider
import com.bytebyte6.view.databinding.ItemDownloadBinding

class DownloadAdapter : BaseAdapter<TvAndDownload, DownloadViewHolder>(DownDiff) {

    var selectionTracker: SelectionTracker<Long>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DownloadViewHolder {
        return DownloadViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: DownloadViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.binding.apply {
            val item = getItem(position)
            tvTitle.text = item.tv.name
            tvBody.text = item.tv.category
            holder.details.pos = position.toLong()
            GlideApp.with(this.ivAvatar)
                .load(item.tv.logo)
                .placeholder(R.drawable.landscape)
                .into(ivAvatar)
            val progress = item.download.percentDownloaded.toInt()
            progressBar.progress = progress
            tvProgress.text = progress.toString().plus("%")
            selectionTracker?.apply {
                val selected = isSelected(holder.details.selectionKey)
                cardView.isChecked = selected
                holder.itemView.setOnClickListener {
                    if (!hasSelection()) {
                        getOnItemClick()?.invoke(position, it)
                    }
                }
            }
        }
    }
}

object DownDiff : DiffUtil.ItemCallback<TvAndDownload>() {
    override fun areItemsTheSame(oldItem: TvAndDownload, newItem: TvAndDownload): Boolean {
        return oldItem.tv.tvId == newItem.tv.tvId
                && oldItem.download.request.id == newItem.download.request.id
    }

    override fun areContentsTheSame(oldItem: TvAndDownload, newItem: TvAndDownload): Boolean {
        return oldItem == newItem
    }
}

class DownloadViewHolder(val binding: ItemDownloadBinding) : RecyclerView.ViewHolder(binding.root) {
    val details = Details()

    companion object {
        fun create(parent: ViewGroup): DownloadViewHolder {
            return DownloadViewHolder(
                ItemDownloadBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }
}