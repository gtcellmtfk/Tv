package com.example.test_ui_factory.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.test_ui_factory.entry.User

interface UserDao {
    @Query("SELECT * FROM User")
    fun allAsLiveData(): LiveData<List<User>>

    @Query("SELECT * FROM User /*WHERE id=:id*/")
    fun asLiveDataById(/*id:Long*/): LiveData<User>

    @Query("SELECT * FROM User LIMIT :pageSize OFFSET :offset")
    fun queryByPaging(offset: Int, pageSize: Int = 20): List<User>

    @Query("SELECT * FROM User /*WHERE id=:id*/")
    fun queryById(/*id:Long*/): User

    @Query("SELECT * FROM User")
    fun queryAll(): List<User>

    @Query("SELECT COUNT(*) FROM User")
    fun queryCount(): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(data: User): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(list: List<User>): List<Long>

    @Delete
    fun delete(data: User)

    @Delete
    fun delete(list: List<User>)

    @Update
    fun update(data: User)

    @Update
    fun update(list: List<User>)
}