package com.bytebyte6.data

import androidx.room.Dao
import androidx.room.Query
import com.bytebyte6.data.model.IpTvFts
import io.reactivex.rxjava3.core.Single

@Dao
interface IpTvFtsDao {
    @Query("SELECT * FROM IpTvFts WHERE IpTvFts MATCH :key ")
    fun search(key: String): Single<List<IpTvFts>>
}