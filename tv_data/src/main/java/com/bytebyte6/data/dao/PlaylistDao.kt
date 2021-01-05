package com.bytebyte6.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.bytebyte6.data.entity.Playlist
import com.bytebyte6.data.entity.PlaylistWithTvs
import com.bytebyte6.data.entity.PlaylistWithUsers

@Dao
abstract class PlaylistDao : BaseDao<Playlist> {
    @Transaction
    @Query("SELECT * FROM Playlist")
    abstract fun getPlaylistsWithTvs(): List<PlaylistWithTvs>

    @Transaction
    @Query("SELECT * FROM Playlist WHERE playlistId=:playlistId")
    abstract fun tvsById(playlistId:Long): LiveData<PlaylistWithTvs>

    @Transaction
    @Query("SELECT * FROM Playlist")
    abstract fun getPlaylistsWithUsers(): List<PlaylistWithUsers>

    @Query("SELECT * FROM Playlist")
    abstract fun getAll(): List<Playlist>

    @Query("SELECT COUNT(*) FROM Playlist")
    abstract fun count(): Int
}