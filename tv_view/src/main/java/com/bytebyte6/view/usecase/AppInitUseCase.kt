package com.bytebyte6.view.usecase

import android.content.Context
import com.bytebyte6.data.Converter
import com.bytebyte6.data.RxUseCase
import com.bytebyte6.data.TvApi
import com.bytebyte6.data.dao.TvDao
import com.bytebyte6.data.entity.Tv
import io.reactivex.rxjava3.core.Single
import java.util.concurrent.TimeUnit

class AppInitUseCase(
    private val converter: Converter,
    private val tvDao: TvDao,
    private val api: TvApi,
    private val context: Context
) : RxUseCase<String, List<Tv>>() {
    override fun getSingle(param: String): Single<List<Tv>> {
        return Single.create<List<Tv>> {
            if (tvDao.count() == 0) {
                val tvs = converter.getTvs(context)
                tvDao.insert(Tv.init(tvs))
            }
            it.onSuccess(emptyList())
        }
            .delay(3,TimeUnit.SECONDS)/*.flatMap {
            api.getList()
        }.doOnSuccess {
            tvDao.insert(Tv.init(it))
        }.delay(0,TimeUnit.SECONDS)*/
    }
}