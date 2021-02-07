package com.bytebyte6.view.adapter

import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.bytebyte6.data.model.LangDiff
import com.bytebyte6.data.model.Languages
import com.bytebyte6.utils.BaseListAdapter
import com.bytebyte6.view.R
import com.bytebyte6.view.randomImage

class LanguageAdapter : BaseListAdapter<Languages, CardViewHolder>(LangDiff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        return CardViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val item = getItem(position)

        holder.apply {
            //重建后的recyclerview Item是没有transName的 所以在onBind要重新赋值一遍 动画效果才会有~~
            itemView.transitionName = item.langName
            tvTitle.text = item.langName
            tvBody.text = item.langCode
            ivIcon.setImageResource(randomImage())

            cardView.apply {
                strokeWidth = 4
                strokeColor = item.color
                radius = 10f
                setCardBackgroundColor(
                    ContextCompat.getColor(
                        holder.itemView.context,
                        R.color.itemRootLayoutBackgroundColor
                    )
                )
            }

        }
    }
}