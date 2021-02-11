package com.bytebyte6.data.entity

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
@Keep
data class User(
    @PrimaryKey(autoGenerate = true)
    val userId: Long = 0,
    var name: String = "",
    var nightMode: Boolean = false,
    var capturePic: Boolean = false,
    var downloadOnlyOnWifi: Boolean = true,
    var playOnlyOnWifi: Boolean = true
) : Parcelable