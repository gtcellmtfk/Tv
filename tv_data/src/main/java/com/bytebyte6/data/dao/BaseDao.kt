package com.bytebyte6.data.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import io.reactivex.rxjava3.core.Single

interface BaseDao<T> {
    @Insert(onConflict = REPLACE)
    fun insert(vararg data: T):LongArray

    @Insert(onConflict = REPLACE)
    fun insert( data: T):Long

    @Insert(onConflict = REPLACE)
    fun insertAll(list: List<T>):List<Long>

    @Delete
    fun delete(data:T)

    @Delete
    fun delete(vararg data:T)

    @Delete
    fun deleteAll(data:T)
}