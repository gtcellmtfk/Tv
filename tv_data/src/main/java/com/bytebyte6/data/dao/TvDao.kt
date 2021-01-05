package com.bytebyte6.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.bytebyte6.data.entity.Tv
import com.bytebyte6.data.entity.TvWithPlaylists
import io.reactivex.rxjava3.core.Single
import org.jetbrains.annotations.TestOnly

const val PAGE_SIZE = 100

@Dao
interface TvDao {

    @Transaction
    @Query("SELECT * FROM Tv")
    fun getTvsWithPlaylists(): List<TvWithPlaylists>

    @Query("SELECT * FROM Tv LIMIT $PAGE_SIZE OFFSET :offset")
    fun paging(offset: Int): List<Tv>

    /**
     * 列出所有分类
     */
    @Query("SELECT DISTINCT category FROM Tv ORDER BY category ASC ")
    fun liveDataByAllCategory(): LiveData<List<String>>

    /**
     * 列出所有语言
     */
    @Query("SELECT DISTINCT language FROM Tv ORDER BY language ASC ")
    fun liveDataByAllLanguage(): LiveData<List<String>>

    /**
     * 列出所有国家
     */
    @Query("SELECT DISTINCT countryName FROM Tv ORDER BY countryName ASC")
    fun liveDataByAllCountry(): LiveData<List<String>>

    /**
     * 根据category获取Iptv总数
     */
    @Query("SELECT COUNT(category) FROM Tv WHERE category=:category")
    fun countByCategory(category: String): Int

    /**
     * 根据language获取Iptv总数
     */
    @Query("SELECT COUNT(language) FROM Tv WHERE language=:language")
    fun countByLanguage(language: String): Int

    /**
     * 根据countryName获取Iptv总数
     */
    @Query("SELECT COUNT(countryName) FROM Tv WHERE countryName=:countryName")
    fun countByCountry(countryName: String): Int

    @Query("SELECT COUNT(tvId) FROM Tv")
    fun count(): Single<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(list: List<Tv>):List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllRx(list: List<Tv>):Single<List<Long>>

    @TestOnly
    @Query("SELECT COUNT(tvId) FROM Tv")
    fun countForTest(): Int

    @TestOnly
    @Query("SELECT * FROM Tv")
    fun getAllForTest(): List<Tv>
}