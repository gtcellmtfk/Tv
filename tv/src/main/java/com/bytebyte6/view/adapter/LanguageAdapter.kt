package com.bytebyte6.view.adapter

import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.bytebyte6.data.entity.Language
import com.bytebyte6.data.model.LangDiff
import com.bytebyte6.utils.BaseListAdapter
import com.bytebyte6.view.R
import com.bytebyte6.view.randomImage

class LanguageAdapter : BaseListAdapter<Language, CardViewHolder>(LangDiff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        return CardViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val item = getItem(position)

        holder.apply {
            // 重建后的recyclerview Item是没有transName的
            // 所以在onBind要重新赋值一遍 动画效果才会有~~
            itemView.transitionName = item.languageCode
            tvTitle.text = item.languageName
            tvBody.text = item.languageCode
            ivIcon.setImageResource(randomImage())
            cardView.strokeWidth = 4
            cardView.strokeColor = item.color
            cardView.radius = 10f
            cardView.setCardBackgroundColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.itemRootLayoutBackgroundColor
                )
            )
        }
    }
}