package com.bytebyte6.usecase

import androidx.annotation.Keep
import com.bytebyte6.base.RxSingleUseCase
import com.bytebyte6.data.dao.TvDao
import com.bytebyte6.data.entity.Tv

class FavoriteTvUseCase(private val tvDao: TvDao) :
    RxSingleUseCase<FavoriteTvParam, FavoriteTvParam>() {
    override fun run(param: FavoriteTvParam): FavoriteTvParam {
        val tv = tvDao.getTv(param.tv.tvId)
        tv.favorite = !tv.favorite
        tvDao.update(tv)
        param.tv = tv
        return param
    }
}

@Keep
data class FavoriteTvParam(
    var pos: Int,
    var tv: Tv
)