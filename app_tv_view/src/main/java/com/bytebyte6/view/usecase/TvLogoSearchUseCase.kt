package com.bytebyte6.view.usecase

import com.bytebyte6.data.RxSingleUseCase
import com.bytebyte6.data.dao.TvDao
import com.bytebyte6.data.work.ImageSearch
import io.reactivex.rxjava3.core.Single

class TvLogoSearchUseCase(
    private val imageSearch: ImageSearch,
    private val tvDao: TvDao
) : RxSingleUseCase<Long, Boolean>() {
    override fun getSingle(tvId: Long): Single<Boolean> {
        return Single.create { emitter ->
            val tv = tvDao.getTv(tvId)
            if (tv.logo.isEmpty()) {
                val logos = imageSearch.search(tv.name)
                if (logos.isNotEmpty()) {
                    tv.logo = logos[0]
                    tvDao.insert(tv)
                }
            }
            emitter.onSuccess(true)
        }
    }
}