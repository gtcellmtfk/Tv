package com.bytebyte6.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.bytebyte6.data.entity.User
import com.bytebyte6.data.entity.UserWithPlaylists
import io.reactivex.rxjava3.core.Single

@Dao
abstract class UserDao : BaseDao<User> {
    @Transaction
    @Query("SELECT * FROM User")
    abstract fun getUsersWithPlaylists(): List<UserWithPlaylists>

    @Transaction
    @Query("SELECT * FROM User WHERE userId=:userId")
    abstract fun getPlaylistsByUserId(userId: Long): UserWithPlaylists

    @Query("SELECT * FROM User")
    abstract fun getAll(): List<User>

    @Query("SELECT * FROM User")
    abstract fun get(): User

    @Query("SELECT COUNT(*) FROM User")
    abstract fun count(): Int
}