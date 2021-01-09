package com.bytebyte6.data.model

import android.os.Parcelable
import androidx.room.Ignore
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Languages(
    var language: List<Language> = emptyList(),
    @Ignore var content: String = ""
) : Parcelable , Card {
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

    override val title: String
        get() = getString()

    override val body: String
        get() = content

    override val outline: Boolean
        get() = true
}