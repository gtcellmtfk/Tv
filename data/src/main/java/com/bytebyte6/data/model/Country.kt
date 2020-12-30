package com.bytebyte6.data.model

import android.os.Parcelable
import androidx.room.Ignore
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Country(
    @SerializedName("code")
    var countryCode: String = "",
    @SerializedName("name")
    var countryName: String = "",
    @SerializedName("count")
    @Ignore var count: Int = 0
) : Parcelable