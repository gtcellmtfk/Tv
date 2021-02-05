package com.bytebyte6.usecase.work

import android.content.Context
import androidx.work.*

/***
 * 搜索国旗和电视台logo的图片链接
 */
class FindImageWork(
    context: Context,
    workerParams: WorkerParameters,
    private val countryImageSearch: CountryImageSearch,
    private val tvLogoSearch: TvLogoSearch
) : Worker(context, workerParams) {

    override fun doWork(): Result {
        try {
            countryImageSearch.doThatShit()
            tvLogoSearch.doThatShit()
        } catch (e: Exception) {
            return Result.failure()
        }
        return Result.success()
    }
}

class AppDelegatingWorkerFactory(
    countryImageSearch: CountryImageSearch,
    tvLogoSearch: TvLogoSearch
) : DelegatingWorkerFactory() {
    init {
        addFactory(GetCountryFactor(countryImageSearch, tvLogoSearch))
    }
}

class GetCountryFactor(
    private val countryImageSearch: CountryImageSearch,
    private val tvLogoSearch: TvLogoSearch
) : WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        return FindImageWork(
            countryImageSearch = countryImageSearch,
            tvLogoSearch = tvLogoSearch,
            context = appContext,
            workerParams = workerParameters
        )
    }
}