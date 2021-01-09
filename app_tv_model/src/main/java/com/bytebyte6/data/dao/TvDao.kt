package com.bytebyte6.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.bytebyte6.data.PAGE_SIZE
import com.bytebyte6.data.entity.Tv
import com.bytebyte6.data.model.Category
import com.bytebyte6.data.model.Languages
import com.bytebyte6.data.model.CountryWithTvs
import com.bytebyte6.data.model.TvWithPlaylists

@Dao
interface TvDao : BaseDao<Tv>{
    @Transaction
    @Query("SELECT * FROM Tv")
    fun getTvsWithPlaylists(): List<TvWithPlaylists>

    @Query("SELECT * FROM Tv LIMIT $PAGE_SIZE OFFSET :offset")
    fun paging(offset: Int): List<Tv>

    @Query("SELECT DISTINCT category FROM Tv ORDER BY category ASC ")
    fun liveDataByAllCategory(): LiveData<List<Category>>

    @Query("SELECT DISTINCT language FROM Tv ORDER BY language ASC ")
    fun liveDataByAllLanguage(): LiveData<List<Languages>>

    @Query("SELECT COUNT(tvId) FROM Tv")
    fun count(): Int

    @Query("SELECT * FROM Tv")
    fun getList(): List<Tv>
}