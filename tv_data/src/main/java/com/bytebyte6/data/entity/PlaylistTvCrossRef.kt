package com.bytebyte6.data.entity

import androidx.room.*

@Entity(primaryKeys = ["playlistId", "tvId"] ,indices = [Index("tvId")])
data class PlaylistTvCrossRef(
    val playlistId: Long,
    val tvId: Long
)
