package com.bytebyte6.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Language(
    @SerializedName("name")
    val languageName: String = "",
    @SerializedName("code")
    val languageCode: String = ""
) : Parcelable, Comparable<Language> {

    override fun compareTo(other: Language): Int {
        return languageName.compareTo(other.languageName)
    }

    override fun toString(): String {
        return languageName
    }
}