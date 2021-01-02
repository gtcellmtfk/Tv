package com.bytebyte6.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Languages(
    var languages: List<Language> = emptyList(),
    var count: Int = 0
) : Parcelable {
    fun getString(): String {
        val stringBuilder = StringBuilder()
        languages.forEachIndexed { index, language ->
            stringBuilder.append(language.languageName)
            if (index != languages.size - 1) {
                stringBuilder.append(" , ")
            }
        }
        return stringBuilder.toString()
    }
}