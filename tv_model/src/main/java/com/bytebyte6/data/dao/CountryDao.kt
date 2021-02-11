package com.bytebyte6.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.bytebyte6.data.entity.Country
import com.bytebyte6.data.model.CountryWithTvs

/**
 * Dao命名规则
 * LiveData直接用结果命名，如获取所有国家命名为countries()
 * 获取List或数据实体前面加get，如getCountry()，getCount(),复数getCountries()
 */
@Dao
interface CountryDao : BaseDao<Country> {
    @Query("SELECT COUNT(countryId) FROM Country")
    fun getCount(): Int

    @Query("SELECT countryId FROM Country WHERE name=:name")
    fun getIdByName(name: String): Long

    @Query("SELECT * FROM Country WHERE image=:image")
    fun getCountriesByImage(image: String): List<Country>

    @Query("SELECT * FROM Country")
    fun getCountries(): List<Country>

    @Transaction
    @Query("SELECT * FROM Country")
    fun getCountryWithTvss(): List<CountryWithTvs>

    @Transaction
    @Query("SELECT * FROM Country WHERE countryId=:countryId")
    fun getCountryWithTvs(countryId: Long): CountryWithTvs

    /**
     * LiveData
     */

    @Transaction
    @Query("SELECT * FROM Country WHERE countryId=:countryId")
    fun countryWithTvs(countryId: Long): LiveData<CountryWithTvs>

    @Query("SELECT * FROM Country ORDER BY name ASC")
    fun countries(): LiveData<List<Country>>
}