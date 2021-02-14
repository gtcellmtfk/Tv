package com.bytebyte6.data.entity

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
@Keep
data class Playlist(
        @PrimaryKey(autoGenerate = true)
        var playlistId: Long = 0,
        var playlistName: String,
        var total: Int = 0
) : Parcelable