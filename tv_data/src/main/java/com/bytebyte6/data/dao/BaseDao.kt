package com.bytebyte6.data.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE

interface BaseDao<T> {
    @Insert(onConflict = REPLACE)
    fun insert(vararg data: T)

    @Insert(onConflict = REPLACE)
    fun insertAll(list: List<T>)

    @Delete
    fun delete(data:T)

    @Delete
    fun delete(vararg data:T)

    @Delete
    fun deleteAll(data:T)
}