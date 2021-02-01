package com.bytebyte6.utils

import androidx.recyclerview.selection.ItemDetailsLookup


class Details : ItemDetailsLookup.ItemDetails<Long>() {

    var pos: Long = 0L

    override fun getSelectionKey(): Long = pos

    override fun getPosition(): Int = pos.toInt()
}

