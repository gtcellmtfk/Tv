package com.bytebyte6.data

import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.core.SingleSource
import io.reactivex.rxjava3.core.SingleTransformer

class LoadDataSingleTransformer<T>(private val loadData: LoadData<T>) : SingleTransformer<T, T> {
    override fun apply(upstream: Single<T>): SingleSource<T> {
        return upstream.doOnSubscribe { loadData.start() }
            .doOnSuccess { loadData.success(it) }
            .doOnError { loadData.fail(it) }
    }
}

fun <T> Single<T>.compose(loadData: LoadData<T>): Single<T> {
    return compose(LoadDataSingleTransformer(loadData))
}