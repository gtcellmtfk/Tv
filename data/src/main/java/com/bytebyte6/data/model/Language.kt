package com.bytebyte6.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Language(
    @SerializedName("code")
    val languageCode: String?,
    @SerializedName("name")
    val languageName: String?
) : Parcelable{
    override fun toString(): String {
        return "Language(languageCode=$languageCode, languageName=$languageName)"
    }
}