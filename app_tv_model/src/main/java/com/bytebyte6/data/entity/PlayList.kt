package com.bytebyte6.data.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.bytebyte6.data.model.Card
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(indices = [Index(value = ["playlistName"], unique = true)])
data class Playlist(
    @PrimaryKey(autoGenerate = true)
    val playlistId: Long = 0,
    var playlistName: String
) : Parcelable, Card {

    override val title: String
        get() = playlistName

    override val body: String
        get() = playlistId.toString()

    override val outline: Boolean
        get() = true
}