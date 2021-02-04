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
    fun getUsers(): List<User>

    @Query("SELECT * FROM User")
    fun getUser(): User

    @Query("SELECT COUNT(*) FROM User")
    fun getCount(): Int

    /**
     * LiveData
     */

    @Query("SELECT * FROM User")
    fun user(): LiveData<User?>

    @Transaction
    @Query("SELECT * FROM User WHERE userId=:userId")
    fun userWithPlaylists(userId: Long): LiveData<UserWithPlaylists>
}