package com.bytebyte6.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.bytebyte6.data.entity.Playlist
import com.bytebyte6.data.model.PlaylistWithTvs
import com.bytebyte6.data.model.PlaylistWithUsers

@Dao
interface PlaylistDao : BaseDao<Playlist> {
    @Transaction
    @Query("SELECT * FROM Playlist")
    fun getPlaylistsWithTvss(): List<PlaylistWithTvs>

    @Transaction
    @Query("SELECT * FROM Playlist WHERE playlistId=:playlistId")
    fun getPlaylistWithTvsById(playlistId: Long): PlaylistWithTvs

    @Transaction
    @Query("SELECT * FROM Playlist")
    fun getPlaylistsWithUsers(): List<PlaylistWithUsers>

    @Query("SELECT * FROM Playlist")
    fun getPlaylists(): List<Playlist>

    /**
     * LiveData
     */

    @Transaction
    @Query("SELECT * FROM Playlist WHERE playlistId=:playlistId")
    fun playlistWithTvs(playlistId: Long): LiveData<PlaylistWithTvs>
}