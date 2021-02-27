package com.bytebyte6.view.adapter

import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.bytebyte6.common.randomColorByNightMode
import com.bytebyte6.data.entity.Playlist
import com.bytebyte6.utils.AdapterHelper
import com.bytebyte6.utils.BaseDetailAdapter
import com.bytebyte6.utils.DragAdapterHelper
import com.bytebyte6.view.R
import com.bytebyte6.view.randomImage

class PlaylistAdapter : BaseDetailAdapter<Playlist, PlaylistViewHolder>(),
    AdapterHelper<Playlist, PlaylistViewHolder> {

    override val adapter: RecyclerView.Adapter<PlaylistViewHolder> = this
    override var selectionTracker: SelectionTracker<Long>? = null
    override var onItemClick: ((pos: Int, view: View) -> Unit)? = null
    override var onItemLongClick: ((pos: Int, view: View) -> Boolean)? = null
    override var onBind: ((pos: Int, view: View) -> Unit)? = null
    override val list: MutableList<Playlist> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        return PlaylistViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)

        val item = list[position]

        holder.apply {
            // 重建后的recyclerview Item是没有transName的
            // 所以在onBind要重新赋值一遍 动画效果才会有~~
            itemView.transitionName = item.hashCode().toString()
            tvTitle.text = item.playlistName
            tvBody.text = itemView.context.getString(R.string.total, item.total)
            ivIcon.setImageResource(randomImage())
            cardView.strokeWidth = 4
            cardView.strokeColor = randomColorByNightMode()
            cardView.radius = 10f
            val color = ContextCompat.getColor(
                itemView.context,
                R.color.itemRootLayoutBackgroundColor
            )
            cardView.setCardBackgroundColor(color)
        }
    }

    override fun adapterHelper(): AdapterHelper<Playlist, PlaylistViewHolder> {
        return this
    }

}