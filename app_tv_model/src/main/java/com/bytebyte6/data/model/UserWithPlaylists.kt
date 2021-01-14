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


@Keep
@Parcelize
data class UserWithPlaylists(
    @Embedded val user: User,
    @Relation(
        parentColumn = "userId",
        entityColumn = "playlistId",
        associateBy = Junction(UserPlaylistCrossRef::class)
    )
    val playlists: List<Playlist> = emptyList()
) : Parcelable
