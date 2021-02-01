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
data class PlaylistWithTvs(
    @Embedded val playlist: Playlist,
    @Relation(
        parentColumn = "playlistId",
        entityColumn = "tvId",
        associateBy = Junction(PlaylistTvCrossRef::class)
    )
    val tvs: List<Tv> = emptyList()
) : Parcelable