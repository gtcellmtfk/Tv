package com.bytebyte6.data.entity

import androidx.room.*

@Entity(primaryKeys = ["userId", "playlistId"],indices = [Index("playlistId")])
data class UserPlaylistCrossRef(
    val userId: Long,
    val playlistId: Long
)
