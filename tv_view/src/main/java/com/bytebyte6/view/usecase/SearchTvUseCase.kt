package com.bytebyte6.view.usecase

import com.bytebyte6.data.RxUseCase
import com.bytebyte6.data.dao.TvFtsDao
import com.bytebyte6.data.entity.Tv
import com.bytebyte6.data.entity.TvFts
import io.reactivex.rxjava3.core.Single

class SearchTvUseCase(private val tvFtsDao: TvFtsDao) : RxUseCase<String, List<Tv>>() {
    override fun getSingle(param: String): Single<List<Tv>> {
        return tvFtsDao.search(param).map {
            TvFts.toIpTvs(it)
        }
    }
}