package com.bytebyte6.library

import androidx.recyclerview.selection.ItemKeyProvider

class KeyProvider : ItemKeyProvider<Long>(SCOPE_MAPPED) {
    override fun getKey(position: Int): Long {
        return position.toLong()
    }

    override fun getPosition(key: Long): Int {
        return key.toInt()
    }
}
