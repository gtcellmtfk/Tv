package com.bytebyte6.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bytebyte6.base.LoadData
import com.bytebyte6.base.mvi.Result
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.core.SingleSource
import io.reactivex.rxjava3.core.SingleTransformer
import io.reactivex.rxjava3.disposables.Disposable
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

abstract class RxUseCase<P, R> {
    private val liveData: MutableLiveData<Result<R>> = MutableLiveData()
    fun liveData(): LiveData<Result<R>> = liveData
    fun execute(param: P): Disposable = getSingle(param)
        .doOnSubscribe { liveData.postValue(Result.Loading) }
        .subscribeOn(Schedulers.io())
        .subscribe({
            liveData.postValue(Result.Success(it))
        }, {
            liveData.postValue(Result.Error(it))
        })

    abstract fun getSingle(param: P): Single<R>
}