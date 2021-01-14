package com.bytebyte6.data.entity

import androidx.annotation.Keep
import androidx.room.*

@Entity(primaryKeys = ["userId", "playlistId"],indices = [Index("playlistId")])
@Keep
data class UserPlaylistCrossRef(
    val userId: Long,
    val playlistId: Long
)
