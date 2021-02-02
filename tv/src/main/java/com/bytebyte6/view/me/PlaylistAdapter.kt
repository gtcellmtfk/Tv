package com.bytebyte6.view.me

import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.bytebyte6.data.model.Card
import com.bytebyte6.utils.AdapterHelper
import com.bytebyte6.utils.BaseDetailAdapter
import com.bytebyte6.view.R
import com.bytebyte6.view.home.randomImage

class PlaylistAdapter : BaseDetailAdapter<Card, PlaylistViewHolder>(),
    AdapterHelper<Card, PlaylistViewHolder> {

    override val list: MutableList<Card> = mutableListOf()
    override val adapter: RecyclerView.Adapter<PlaylistViewHolder> = this
    override var itemTouchHelper: ItemTouchHelper? = null
    override var selectionTracker: SelectionTracker<Long>? = null
    override var onItemClick: ((pos: Int, view: View) -> Unit)? = null
    override var onItemLongClick: ((pos: Int, view: View) -> Boolean)? = null
    override var onBind: ((pos: Int, view: View) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        return PlaylistViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val item = list[position]

        holder.apply {
            //重建后的recyclerview Item是没有transName的 所以在onBind要重新赋值一遍 动画效果才会有~~
            itemView.transitionName = item.transitionName
            tvTitle.text = item.title
            tvBody.text = item.body
            ivIcon.setImageResource(randomImage())
            if (item.outline) {
                cardView.apply {
                    strokeWidth = 4
                    strokeColor = item.color
                    radius = item.radius.toFloat()
                    setCardBackgroundColor(
                        ContextCompat.getColor(
                            holder.itemView.context,
                            R.color.itemRootLayoutBackgroundColor
                        )
                    )
                }
            } else {
                cardView.apply {
                    strokeWidth = 0
                    strokeColor = 0
                    radius = item.radius.toFloat()
                    setCardBackgroundColor(item.color)
                }
            }
        }
    }

    override fun adapterHelper(): AdapterHelper<Card, PlaylistViewHolder> {
       return this
    }
}