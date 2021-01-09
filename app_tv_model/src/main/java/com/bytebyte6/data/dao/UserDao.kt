package com.bytebyte6.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.bytebyte6.data.entity.User
import com.bytebyte6.data.model.UserWithPlaylists

@Dao
interface UserDao : BaseDao<User> {
    @Transaction
    @Query("SELECT * FROM User")
    fun getUsersWithPlaylists(): List<UserWithPlaylists>

    @Transaction
    @Query("SELECT * FROM User WHERE userId=:userId")
    fun getPlaylistsByUserId(userId: Long): UserWithPlaylists

    @Query("SELECT * FROM User")
    fun getAll(): List<User>

    @Query("SELECT * FROM User")
    fun get(): User

    @Query("SELECT COUNT(*) FROM User")
    fun count(): Int

    /**
     * LiveData
     */

    @Query("SELECT * FROM User")
    fun liveData(): LiveData<User>

    @Transaction
    @Query("SELECT * FROM User WHERE userId=:userId")
    fun playlistsLiveData(userId: Long): LiveData<UserWithPlaylists>
}