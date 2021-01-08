package com.bytebyte6.view.usecase

import android.content.Context
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.bytebyte6.data.Converter
import com.bytebyte6.data.RxSingleUseCase
import com.bytebyte6.data.dao.CountryDao
import com.bytebyte6.data.dao.TvDao
import com.bytebyte6.data.entity.Country
import com.bytebyte6.data.entity.Tv
import com.bytebyte6.data.work.GetCountryImageWork
import io.reactivex.rxjava3.core.Single
import java.util.concurrent.TimeUnit

class AppInitUseCase(
    private val converter: Converter,
    private val tvDao: TvDao,
    private val countryDao: CountryDao,
    private val context: Context
) : RxSingleUseCase<String, List<Tv>>() {

    private val workRequest = OneTimeWorkRequestBuilder<GetCountryImageWork>().build()

    override fun getSingle(param: String): Single<List<Tv>> {
        return Single.create<List<Tv>> { emitter ->
            if (tvDao.count() == 0) {
                val tvs = converter.getTvs(context)
                val cs = mutableSetOf<Country>()
                tvs.forEach {
                    it.countryName=it.country.name
                    cs.add(it.country)
                }
                countryDao.insert(cs)
                val newTvs = tvs.map {
                    val name = it.country.name
                    if (name.isNotEmpty()) {
                        it.countryId = countryDao.getIdByName(name)
                    }
                    it
                }
                tvDao.insert(newTvs)
                loadImage()
            } else {
                loadImage()
            }
            emitter.onSuccess(emptyList())
        }
            .delay(1, TimeUnit.SECONDS)
    }

    private fun loadImage() {
        WorkManager.getInstance(context)
            .beginUniqueWork("AppInit", ExistingWorkPolicy.KEEP, workRequest)
            .enqueue()
    }


}

