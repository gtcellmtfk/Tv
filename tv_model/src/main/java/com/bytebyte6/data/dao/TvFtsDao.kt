package com.bytebyte6.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.bytebyte6.data.PAGE_SIZE
import com.bytebyte6.data.entity.Tv
import com.bytebyte6.data.entity.TvFts

@Dao
interface TvFtsDao {
    @Query("SELECT * FROM TvFts WHERE TvFts MATCH :key ")
    fun search(key: String): List<Tv>

    @Query("SELECT * FROM TvFts WHERE TvFts MATCH :key ")
    fun tvs(key: String): LiveData<List<Tv>>

    @Query("SELECT COUNT(*) FROM TvFts WHERE TvFts MATCH :key ")
    fun getCount(key: String): Int

    @Query("SELECT * FROM TvFts WHERE TvFts MATCH :key LIMIT :pageSize OFFSET :offset")
    fun paging(offset: Int, key: String, pageSize: Int = PAGE_SIZE): List<Tv>

    /**
     * LiveData
     */

    @Query("SELECT COUNT(*) FROM TvFts WHERE TvFts MATCH :key ")
    fun count(key: String): LiveData<Int>
}