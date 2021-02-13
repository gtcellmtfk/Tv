package com.bytebyte6.data.entity

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.Fts4
import kotlinx.android.parcel.Parcelize

@Parcelize
@Fts4(contentEntity = Tv::class)
@Entity
@Keep
data class TvFts(
    val tvId: Long,
    var url: String,
    var category: String = "",
    var logo: String = "",
    var name: String = "",
    var favorite: Boolean = false,
    var download: Boolean = false,
    var countryId: Long = 0,
    var countryName: String = "",
    var countryCode: String = "",
    var language: String = ""
) : Parcelable