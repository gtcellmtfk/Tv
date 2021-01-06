package com.bytebyte6.data.model

import android.os.Parcelable
import androidx.room.Ignore
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Languages(
    var language: List<Language> = emptyList(),
    @Ignore var count: Int = 0
) : Parcelable {
    fun getString(): String {
        val stringBuilder = StringBuilder()
        language.forEachIndexed { index, language ->
            stringBuilder.append(language.languageName)
            if (index != this.language.size - 1) {
                stringBuilder.append(" , ")
            }
        }
        return stringBuilder.toString()
    }
}