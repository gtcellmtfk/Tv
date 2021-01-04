package com.bytebyte6.data.dao

import androidx.room.*
import com.bytebyte6.data.entity.Playlist
import com.bytebyte6.data.entity.PlaylistWithTvs
import com.bytebyte6.data.entity.PlaylistWithUsers
import com.bytebyte6.data.entity.Tv

@Dao
abstract class PlaylistDao : BaseDao<Playlist>{
    @Transaction
    @Query("SELECT * FROM Playlist")
    abstract  fun getPlaylistsWithTvs(): List<PlaylistWithTvs>

    @Transaction
    @Query("SELECT * FROM Playlist")
    abstract  fun getPlaylistsWithUsers(): List<PlaylistWithUsers>

    @Query("SELECT * FROM Playlist")
    abstract  fun getAll(): List<Playlist>
}