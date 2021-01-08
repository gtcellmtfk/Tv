package com.bytebyte6.view.usecase

import com.bytebyte6.data.RxSingleUseCase
import com.bytebyte6.data.TvApi
import com.bytebyte6.data.dao.TvDao
import io.reactivex.rxjava3.core.Single

/**
 * 更新本地数据库数据
 */
class TvRefreshUseCase(private val api: TvApi, private val tvDao: TvDao) :
    RxSingleUseCase<String, Boolean>() {
    override fun getSingle(param: String): Single<Boolean> {
        return api.getList().map { list ->
            tvDao.insert(list.map {
                it.countryName = it.country.name
                it
            })
            true
        }
    }
}