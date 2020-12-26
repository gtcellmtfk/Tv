package com.bytebyte6.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bytebyte6.data.model.IpTv
import io.reactivex.rxjava3.core.Single

@Dao
interface IpTvDao {
    @Query("SELECT * FROM ipTvs")
    fun getAll(): List<IpTv>

    @Query("SELECT * FROM ipTvs LIMIT 1000 OFFSET :offset")
    fun paging(offset: Int):List<IpTv>

    @Query("SELECT * FROM ipTvs")
    fun allLiveData(): LiveData<List<IpTv>>

    @Query("SELECT * FROM ipTvs LIMIT 1000 OFFSET :offset")
    fun pagingLiveData(offset: Int): LiveData<List<IpTv>>

    @Query("SELECT * FROM ipTvs WHERE category=:category")
    fun liveDataByCategory(category: String): LiveData<List<IpTv>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(list: List<IpTv>)

    @Query("SELECT * FROM ipTvs LIMIT 1")
    fun getAnyIpTv(): Single<IpTv?>
}

