package com.bytebyte6.usecase

import androidx.annotation.Keep
import com.bytebyte6.common.RxUseCase
import com.bytebyte6.data.dao.TvDao
import com.bytebyte6.data.entity.Tv

class UpdateTvUseCase(private val tvDao: TvDao) : RxUseCase<UpdateTvParam, UpdateTvParam>() {
    override fun run(param: UpdateTvParam): UpdateTvParam {
        val tv = tvDao.getTv(param.tv.tvId)
        tv.favorite = param.tv.favorite
        tv.download = param.tv.download
        tvDao.update(tv)
        param.tv = tv
        return param
    }
}

@Keep
data class UpdateTvParam(
    var pos: Int,
    var tv: Tv
)