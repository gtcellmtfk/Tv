package com.bytebyte6.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.bytebyte6.data.PAGE_SIZE
import com.bytebyte6.data.entity.TvFts

@Dao
interface TvFtsDao {
    @Query("SELECT * FROM TvFts WHERE TvFts MATCH :key ")
    fun search(key: String): List<TvFts>

    @Query("SELECT COUNT(*) FROM TvFts WHERE TvFts MATCH :key ")
    fun getCount(key: String): Int

    @Query("SELECT * FROM TvFts WHERE TvFts MATCH :key LIMIT :pageSize OFFSET :offset")
    fun paging(offset: Int, key: String, pageSize: Int = PAGE_SIZE): List<TvFts>

    /**
     * LiveData
     */

    @Query("SELECT COUNT(*) FROM TvFts WHERE TvFts MATCH :key ")
    fun count(key: String): LiveData<Int>
}