package com.bytebyte6.data.model

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.bytebyte6.data.entity.Playlist
import com.bytebyte6.data.entity.PlaylistTvCrossRef
import com.bytebyte6.data.entity.Tv
import kotlinx.android.parcel.Parcelize


@Parcelize
@Keep
data class TvWithPlaylists(
    @Embedded val tv: Tv,
    @Relation(
        parentColumn = "tvId",
        entityColumn = "playlistId",
        associateBy = Junction(PlaylistTvCrossRef::class)
    )
    val playlists: List<Playlist> = emptyList()
) : Parcelable
