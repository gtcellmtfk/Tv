package com.bytebyte6.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.bytebyte6.data.entity.User
import com.bytebyte6.data.entity.UserWithPlaylists

@Dao
abstract class UserDao : BaseDao<User> {
    @Transaction
    @Query("SELECT * FROM User")
    abstract fun getUsersWithPlaylists(): List<UserWithPlaylists>

    @Query("SELECT * FROM User")
    abstract fun getAll(): List<User>
}