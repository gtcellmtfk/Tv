package com.bytebyte6.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Category(
    @SerializedName("category")
    var category: String = "",
    @SerializedName("count")
    var count: Int = 0
) : Parcelable