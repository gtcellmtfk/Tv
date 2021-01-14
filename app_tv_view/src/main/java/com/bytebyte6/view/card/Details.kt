package com.bytebyte6.view.card

import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.widget.DiffUtil
import com.bytebyte6.data.model.Card

class Details : ItemDetailsLookup.ItemDetails<Long?>() {
    var pos: Long? = null

    override fun getSelectionKey(): Long? = pos

    override fun getPosition(): Int = if (pos == null) 0 else pos!!.toInt()
}

object CardDiff : DiffUtil.ItemCallback<Card>() {
    override fun areItemsTheSame(
        oldItem: Card,
        newItem: Card
    ): Boolean = oldItem.title == oldItem.title

    override fun areContentsTheSame(
        oldItem: Card,
        newItem: Card
    ): Boolean = oldItem.title == oldItem.title && oldItem.body == oldItem.body
}


