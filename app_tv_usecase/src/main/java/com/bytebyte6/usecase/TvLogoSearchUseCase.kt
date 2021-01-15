package com.bytebyte6.usecase

import com.bytebyte6.base.RxSingleUseCase
import com.bytebyte6.data.dao.TvDao
import com.bytebyte6.data.work.ImageSearch

class TvLogoSearchUseCase(
    private val imageSearch: ImageSearch,
    private val tvDao: TvDao
) : RxSingleUseCase<Long, Boolean>() {
    override fun doSomething(param: Long): Boolean {
        val tv = tvDao.getTv(param)
        if (tv.logo.isEmpty()) {
            val logos = imageSearch.search(tv.name)
            if (logos.isNotEmpty()) {
                tv.logo = logos[0]
                tvDao.insert(tv)
            }
        }
        return true
    }
}