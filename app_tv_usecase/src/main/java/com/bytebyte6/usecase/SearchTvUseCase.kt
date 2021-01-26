package com.bytebyte6.usecase

import com.bytebyte6.base.RxUseCase
import com.bytebyte6.data.dao.TvFtsDao
import com.bytebyte6.data.entity.Tv
import com.bytebyte6.data.entity.TvFts

class SearchTvUseCase(private val tvFtsDao: TvFtsDao) : RxUseCase<String, List<Tv>>() {
    override fun run(param: String): List<Tv> {
        val list = tvFtsDao.search(param)
        return list.map {
            TvFts.toTv(it)
        }
    }
}