package com.bytebyte6.data.model

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
@Keep
data class Language(
    @SerializedName("name")
    var languageName: String = "",
    @SerializedName("code")
    var languageCode: String = ""
) : Parcelable {
    companion object {
        private const val NAME = "Unkown"
        private const val CODE = "Unkown"
        val DEFAULT = Language(NAME, CODE)
    }
}