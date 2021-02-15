package com.bytebyte6.view.adapter

import android.view.ViewGroup
import com.bytebyte6.data.entity.Category
import com.bytebyte6.data.entity.CategoryDiff
import com.bytebyte6.utils.BaseListAdapter
import com.bytebyte6.view.R
import com.bytebyte6.view.randomImage

class CategoryAdapter : BaseListAdapter<Category, CardViewHolder>(
    CategoryDiff
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        return CardViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val item = getItem(position)

        holder.apply {
            // 重建后的recyclerview Item是没有transName的
            // 所以在onBind要重新赋值一遍 动画效果才会有~~
            itemView.transitionName = item.category
            tvTitle.text = item.category
            tvBody.setText(getBody(item.category))
            ivIcon.setImageResource(randomImage())
            cardView.strokeWidth = 0
            cardView.strokeColor = 0
            cardView.radius = 10f
            cardView.setCardBackgroundColor(item.color)
        }
    }
}

private fun getBody(category: String): Int {
    return when (category) {
        "Music" -> R.string.category_music
        "Auto" -> R.string.category_auto
        "News" -> R.string.category_news
        "Local" -> R.string.category_local
        "Movies" -> R.string.category_movies
        "Entertainment" -> R.string.category_entertainment
        "General" -> R.string.category_general
        "Religious" -> R.string.category_religious
        "Sport" -> R.string.category_sport
        "Comedy" -> R.string.category_comedy
        "Hobby" -> R.string.category_hobby
        "Kids" -> R.string.category_kids
        "Legislative" -> R.string.category_legislative
        "Business" -> R.string.category_business
        "Lifestyle" -> R.string.category_lifestyle
        "Travel" -> R.string.category_travel
        "Education" -> R.string.category_education
        "Documentary" -> R.string.category_documentary
        "Shop" -> R.string.category_shop
        "Fashion" -> R.string.category_fashion
        "History" -> R.string.category_history
        "Family" -> R.string.category_family
        "Weather" -> R.string.category_weather
        "Health" -> R.string.category_health
        else -> R.string.category_other
    }
}