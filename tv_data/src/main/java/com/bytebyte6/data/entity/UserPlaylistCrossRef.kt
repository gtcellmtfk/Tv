package com.bytebyte6.data.entity

import androidx.room.*

@Entity(primaryKeys = ["userId", "playlistId"],indices = [Index("playlistId")])
data class UserPlaylistCrossRef(
    val userId: Long,
    val playlistId: Long
)

data class PlaylistWithUsers(
    @Embedded val playlist: Playlist,
    @Relation(
        parentColumn = "playlistId",
        entityColumn = "userId",
        associateBy = Junction(UserPlaylistCrossRef::class)
    )
    val users: List<User> = emptyList()
)

data class UserWithPlaylists(
    @Embedded val user: User,
    @Relation(
        parentColumn = "userId",
        entityColumn = "playlistId",
        associateBy = Junction(UserPlaylistCrossRef::class)
    )
    val playlists: List<Playlist> = emptyList()
)
