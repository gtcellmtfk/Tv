package com.bytebyte6.view.adapter

import androidx.recyclerview.widget.DiffUtil
import com.bytebyte6.data.model.Card

object CardDiff : DiffUtil.ItemCallback<Card>() {
    override fun areItemsTheSame(
        oldItem: Card,
        newItem: Card
    ): Boolean = oldItem.title == oldItem.title

    override fun areContentsTheSame(
        oldItem: Card,
        newItem: Card
    ): Boolean = oldItem.title == oldItem.title
            && oldItem.body == oldItem.body
            && oldItem.outline == oldItem.outline
            && oldItem.color == oldItem.color
            && oldItem.radius == oldItem.radius
            && oldItem.transitionName == oldItem.transitionName
}