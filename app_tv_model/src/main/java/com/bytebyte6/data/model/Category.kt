package com.bytebyte6.data.model

import android.os.Parcelable
import androidx.room.Ignore
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Category(
    var category: String="",
    @Ignore var content: String=""
) : Parcelable, Card {

    override val title: String
        get() = category

    override val body: String
        get() = content

    override val outline: Boolean
        get() = true
}