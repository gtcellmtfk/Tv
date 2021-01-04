package com.bytebyte6.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.bytebyte6.data.entity.TvFts
import io.reactivex.rxjava3.core.Single

@Dao
interface TvFtsDao {
    @Query("SELECT * FROM TvFts WHERE TvFts MATCH :key ")
    fun search(key: String): Single<List<TvFts>>

    @Query("SELECT * FROM TvFts WHERE TvFts MATCH :key ")
    fun searchLiveData(key: String): LiveData<List<TvFts>>
}