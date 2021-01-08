package com.bytebyte6.data.work

import android.content.Context
import androidx.work.*
import com.bytebyte6.base.logd

class GetCountryImageWork(
    context: Context,
    workerParams: WorkerParameters,
    private val countryImageSearch: CountryImageSearch
) :
    Worker(context, workerParams) {

    override fun doWork(): Result {
        logd("work start...")
        countryImageSearch.doThatShit()
        logd("work complete...")
        return Result.success()
    }
}

class AppDelegatingWorkerFactory(countryImageSearch: CountryImageSearch) :
    DelegatingWorkerFactory() {
    init {
        addFactory(GetCountryFactor(countryImageSearch))
    }
}

class GetCountryFactor(private val countryImageSearch: CountryImageSearch) : WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        return GetCountryImageWork(
            countryImageSearch = countryImageSearch,
            context = appContext,
            workerParams = workerParameters
        )
    }
}