package com.bytebyte6.view.download

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bytebyte6.common.GlideClearHelper
import com.bytebyte6.common.ImageClearHelper
import com.bytebyte6.utils.BaseListAdapter
import com.bytebyte6.usecase.TvAndDownload
import com.bytebyte6.view.databinding.ItemDownloadBinding
import java.math.RoundingMode

class DownloadAdapter(private val imageClearHelper: ImageClearHelper = GlideClearHelper()) :
    BaseListAdapter<TvAndDownload, DownloadViewHolder>(DownDiff),
    ImageClearHelper by imageClearHelper {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DownloadViewHolder {
        return DownloadViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: DownloadViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.binding.apply {
            val item = getItem(position)
            tvTitle.text = item.tv.name
            tvBody.text = item.tv.category
            images.add(ivAvatar)
            val progress = item.download.percentDownloaded
                .toBigDecimal()
                .setScale(0, RoundingMode.HALF_UP)
            val progressInt = progress.toInt()
            progressBar.progress = progressInt
            tvProgress.text = progressInt.toString().plus("%")
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