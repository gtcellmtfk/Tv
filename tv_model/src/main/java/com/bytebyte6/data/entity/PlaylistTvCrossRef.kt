package com.bytebyte6.data.entity

import androidx.annotation.Keep
import androidx.room.*

@Entity(primaryKeys = ["playlistId", "tvId"] ,indices = [Index("tvId")])
@Keep
data class PlaylistTvCrossRef(
    val playlistId: Long,
    val tvId: Long
)
