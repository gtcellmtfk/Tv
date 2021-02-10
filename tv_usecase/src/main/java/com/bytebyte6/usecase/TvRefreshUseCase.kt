package com.bytebyte6.usecase

import com.bytebyte6.data.DataManager
import com.bytebyte6.data.TvApi
import com.bytebyte6.data.entity.Tv
import io.reactivex.rxjava3.core.Single

/**
 * 更新本地数据库数据
 */
class TvRefreshUseCase(private val api: TvApi, private val dataManager: DataManager) {
    fun getSingle(): Single<Boolean> {
        return api.getTvs().map { list ->
            dataManager.insertTv(Tv.inits(list))
            true
        }
    }
}