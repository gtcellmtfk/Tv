package com.bytebyte6.view.card

import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.bytebyte6.data.model.Card
import com.bytebyte6.library.BaseListAdapter
import com.bytebyte6.view.R
import com.bytebyte6.view.home.randomImage

class CardAdapter : BaseListAdapter<Card, CardViewHolder>(CardDiff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        return CardViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val item = getItem(position)

        holder.apply {
            //重建后的recyclerview Item是没有transName的 所以在onBind要重新赋值一遍 动画效果才会有~~
            itemView.transitionName = item.transitionName
            tvTitle.text = item.title
            tvBody.text = item.body
            ivIcon.setImageResource(randomImage())
            if (getItem(position).outline) {
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