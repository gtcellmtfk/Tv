package com.bytebyte6.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bytebyte6.data.model.IpTv
import com.bytebyte6.data.model.WrapLanguages
import io.reactivex.rxjava3.core.Single

const val PAGE_SIZE = 100

@Dao
interface IpTvDao {

    @Query("SELECT * FROM ipTvs LIMIT $PAGE_SIZE OFFSET :offset")
    fun paging(offset: Int): List<IpTv>

    @Query("SELECT * FROM ipTvs LIMIT $PAGE_SIZE OFFSET :offset")
    fun pagingLiveData(offset: Int): LiveData<List<IpTv>>

    @Query("SELECT * FROM ipTvs")
    fun allLiveData(): LiveData<List<IpTv>>

    /**
     * 列出所有分类
     */
    @Query("SELECT DISTINCT category FROM ipTvs ORDER BY category ASC ")
    fun liveDataByAllCategory(): LiveData<List<String>>

    /**
     * 列出所有语言
     */
    @Query("SELECT DISTINCT language FROM ipTvs ORDER BY language ASC ")
    fun liveDataByAllLanguage(): LiveData<List<WrapLanguages>>

    /**
     * 列出所有国家
     */
    @Query("SELECT DISTINCT countryName FROM ipTvs ORDER BY countryName ASC")
    fun liveDataByAllCountry(): LiveData<List<String>>

    /**
     * 根据category列出所有Iptv
     */
    @Query("SELECT * FROM ipTvs WHERE category=:category")
    fun liveDataByCategory(category: String): LiveData<List<IpTv>>

    /**
     * 根据language列出所有Iptv
     */
    @Query("SELECT * FROM ipTvs WHERE language=:language")
    fun liveDataByLanguage(language: String): LiveData<List<IpTv>>

    /**
     * 根据countryName列出所有Iptv
     */
    @Query("SELECT * FROM ipTvs WHERE countryName=:countryName")
    fun liveDataByCountry(countryName: String): LiveData<List<IpTv>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(list: List<IpTv>)

    @Query("SELECT * FROM ipTvs LIMIT 1")
    fun getAnyIpTv(): Single<IpTv>
}