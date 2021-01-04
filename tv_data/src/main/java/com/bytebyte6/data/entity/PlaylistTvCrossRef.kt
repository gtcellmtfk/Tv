package com.bytebyte6.data.entity

import androidx.room.*

@Entity(primaryKeys = ["playlistId", "tvId"] ,indices = [Index("tvId")])
data class PlaylistTvCrossRef(
    val playlistId: Long,
    val tvId: Long
)

data class PlaylistWithTvs(
    @Embedded val playlist: Playlist,
    @Relation(
        parentColumn = "playlistId",
        entityColumn = "tvId",
        associateBy = Junction(PlaylistTvCrossRef::class)
    )
    val tvs: List<Tv> = emptyList()
)

data class TvWithPlaylists(
    @Embedded val tv: Tv,
    @Relation(
        parentColumn = "tvId",
        entityColumn = "playlistId",
        associateBy = Junction(PlaylistTvCrossRef::class)
    )
    val playlists: List<Playlist> = emptyList()
)
