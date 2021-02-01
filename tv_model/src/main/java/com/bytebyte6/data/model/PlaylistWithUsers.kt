package com.bytebyte6.data.model

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.bytebyte6.data.entity.Playlist
import com.bytebyte6.data.entity.User
import com.bytebyte6.data.entity.UserPlaylistCrossRef
import kotlinx.android.parcel.Parcelize

@Parcelize
@Keep
data class PlaylistWithUsers(
    @Embedded val playlist: Playlist,
    @Relation(
        parentColumn = "playlistId",
        entityColumn = "userId",
        associateBy = Junction(UserPlaylistCrossRef::class)
    )
    val users: List<User> = emptyList()
) : Parcelable