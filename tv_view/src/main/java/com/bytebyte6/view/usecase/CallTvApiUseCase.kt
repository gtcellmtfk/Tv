package com.bytebyte6.view.usecase

import com.bytebyte6.data.RxUseCase
import com.bytebyte6.data.TvApi
import com.bytebyte6.data.dao.TvDao
import com.bytebyte6.data.entity.Tv
import io.reactivex.rxjava3.core.Single

class CallTvApiUseCase(private val api: TvApi, private val tvDao: TvDao) :
    RxUseCase<String, Boolean>() {
    override fun getSingle(param: String): Single<Boolean> {
        return api.getList().map { list ->
            tvDao.insert(Tv.init(list))
            true
        }
    }
}