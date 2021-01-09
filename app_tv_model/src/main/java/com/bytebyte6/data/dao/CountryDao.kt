package com.bytebyte6.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.bytebyte6.data.entity.Country
import com.bytebyte6.data.model.CountryWithTvs

@Dao
interface CountryDao : BaseDao<Country> {
    @Query("SELECT COUNT(countryId) FROM Country")
    fun count(): Int

    @Query("SELECT countryId FROM Country WHERE name=:name")
    fun getIdByName(name:String): Long

    @Query("SELECT * FROM Country")
    fun getCountries(): List<Country>

    @Query("SELECT * FROM Country")
    fun countries(): LiveData<List<Country>>

    @Transaction
    @Query("SELECT * FROM Country")
    fun getCountryWithTvs(): List<CountryWithTvs>

    @Transaction
    @Query("SELECT * FROM Country WHERE countryId=:countryId")
    fun getCountryWithTv(countryId: Long): CountryWithTvs

    @Transaction
    @Query("SELECT * FROM Country WHERE countryId=:countryId")
    fun countryWithTv(countryId: Long): LiveData<CountryWithTvs>

}