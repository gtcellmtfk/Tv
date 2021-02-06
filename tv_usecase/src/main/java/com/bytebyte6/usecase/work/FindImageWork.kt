package com.bytebyte6.usecase.work

import android.content.Context
import androidx.work.*

/***
 * 搜索国旗和电视台logo的图片链接
 */
class FindImageWork(
    context: Context,
    workerParams: WorkerParameters,
    private val searchCountryImage: SearchCountryImage,
    private val searchTvLogo: SearchTvLogo
) : Worker(context, workerParams) {

    override fun doWork(): Result {
        try {
            searchCountryImage.searchCountryImage()
            searchTvLogo.searchLogo()
        } catch (e: Exception) {
            return Result.failure()
        }
        return Result.success()
    }
}

class AppDelegatingWorkerFactory(
    searchCountryImage: SearchCountryImage,
    searchTvLogo: SearchTvLogo
) : DelegatingWorkerFactory() {
    init {
        addFactory(GetCountryFactory(searchCountryImage, searchTvLogo))
    }
}

class GetCountryFactory(
    private val searchCountryImage: SearchCountryImage,
    private val searchTvLogo: SearchTvLogo
) : WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        return FindImageWork(
            searchCountryImage = searchCountryImage,
            searchTvLogo = searchTvLogo,
            context = appContext,
            workerParams = workerParameters
        )
    }
}