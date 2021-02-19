package com.bytebyte6.data.entity

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.bytebyte6.common.randomColorByNightMode
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize

@Parcelize
@Keep
@Entity
data class Language(
    @SerializedName("name")
    var languageName: String = "",
    var languageNameChinese: String = "",
    @PrimaryKey
    @SerializedName("code")
    var languageCode: String = ""
) : Parcelable {
    companion object {
        const val UNKOWN = "Unkown"
    }
}