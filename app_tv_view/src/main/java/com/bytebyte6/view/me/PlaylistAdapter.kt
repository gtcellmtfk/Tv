package com.bytebyte6.view.me

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.ItemTouchHelper
import com.bytebyte6.base_ui.BaseListAdapter
import com.bytebyte6.data.model.Card
import com.bytebyte6.library.BaseAdapter
import com.bytebyte6.view.R
import com.bytebyte6.view.card.CardViewHolder
import com.bytebyte6.view.home.randomImage

class PlaylistAdapter : BaseAdapter<Card, PlaylistViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        return PlaylistViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val item = list[position]

        holder.apply {
            //重建后的recyclerview Item是没有transName的 所以在onbind要重新赋值一遍 动画效果才会有~~
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
}