package com.bytebyte6.data

import com.bytebyte6.base.LoadData
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.core.SingleSource
import io.reactivex.rxjava3.core.SingleTransformer
import io.reactivex.rxjava3.schedulers.Schedulers

class LoadDataSingleTransformer<T>(private val loadData: LoadData<T>?) : SingleTransformer<T, T> {
    override fun apply(upstream: Single<T>): SingleSource<T> {
        return upstream
            .subscribeOn(Schedulers.io())
            .doOnSubscribe { loadData?.start() }
            .doOnSuccess { loadData?.success(it) }
            .doOnError { loadData?.fail(it) }
    }
}

fun <T> Single<T>.compose(loadData: LoadData<T>?): Single<T> {
    return compose(LoadDataSingleTransformer(loadData))
}