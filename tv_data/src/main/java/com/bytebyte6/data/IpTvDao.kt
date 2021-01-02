package com.bytebyte6.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bytebyte6.data.model.IpTv
import io.reactivex.rxjava3.core.Single

const val PAGE_SIZE = 100

@Dao
interface IpTvDao {

    @Query("SELECT * FROM IpTvs LIMIT $PAGE_SIZE OFFSET :offset")
    fun paging(offset: Int): List<IpTv>

    /**
     * 列出所有分类
     */
    @Query("SELECT DISTINCT category FROM IpTvs ORDER BY category ASC ")
    fun liveDataByAllCategory(): LiveData<List<String>>

    /**
     * 列出所有语言
     */
    @Query("SELECT DISTINCT language FROM IpTvs ORDER BY language ASC ")
    fun liveDataByAllLanguage(): LiveData<List<String>>

    /**
     * 列出所有国家
     */
    @Query("SELECT DISTINCT countryName FROM IpTvs ORDER BY countryName ASC")
    fun liveDataByAllCountry(): LiveData<List<String>>

    /**
     * 列出所有空值Iptv
     */
    @Query("SELECT * FROM IpTvs WHERE category=:category")
    fun liveDataByCategory(category: String): LiveData<List<IpTv>>

    /**
     * 列出所有空值Iptv
     */
    @Query("SELECT * FROM IpTvs WHERE language=:language")
    fun liveDataByLanguage(language: String): LiveData<List<IpTv>>

    /**
     * 根据category获取Iptv总数
     */
    @Query("SELECT COUNT(category) FROM IpTvs WHERE category=:category")
    fun countByCategory(category: String): Int

    /**
     * 根据language获取Iptv总数
     */
    @Query("SELECT COUNT(language) FROM IpTvs WHERE language=:language")
    fun countByLanguage(language: String): Int

    /**
     * 根据countryName获取Iptv总数
     */
    @Query("SELECT COUNT(countryName) FROM IpTvs WHERE countryName=:countryName")
    fun countByCountry(countryName: String): Int

    @Query("SELECT * FROM IpTvs LIMIT 1")
    fun getAnyIpTv(): Single<IpTv>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(list: List<IpTv>)
}