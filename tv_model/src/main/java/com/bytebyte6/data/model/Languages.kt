package com.bytebyte6.data.model

import androidx.recyclerview.widget.DiffUtil
import com.bytebyte6.data.entity.Language

object LangDiff : DiffUtil.ItemCallback<Language>() {
    override fun areItemsTheSame(oldItem: Language, newItem: Language): Boolean {
        return oldItem.languageCode == newItem.languageCode
    }

    override fun areContentsTheSame(oldItem: Language, newItem: Language): Boolean {
        return oldItem == newItem
    }
}