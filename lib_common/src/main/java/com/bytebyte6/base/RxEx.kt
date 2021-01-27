package com.bytebyte6.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

abstract class RxUseCase<Param, ResultType> {

    private val result: MutableLiveData<Result<ResultType>> = MutableLiveData()

    fun result(): LiveData<Result<ResultType>> = result

    fun execute(param: Param): Single<ResultType> = Single.create<ResultType> {
        try {
            result.postValue((Result.Loading()))
            val resultType = run(param)
            result.postValue((Result.Success(resultType)))
            it.onSuccess(resultType)
        } catch (e: Exception) {
            result.postValue((Result.Error(e)))
            it.onError(e)
        }
    }

    fun interval(param: Param, period: Long = 2): Observable<Long> =
        Observable.interval(period, TimeUnit.SECONDS)
            .doOnNext {
                val resultType = run(param)
                result.postValue((Result.Success(resultType)))
            }.doOnError {
                result.postValue((Result.Error(it)))
            }

    abstract fun run(param: Param): ResultType
}

fun <T> Single<T>.onIo(): Disposable {
    return subscribeOn(Schedulers.io())
        .subscribe({}, { it.printStackTrace() })
}

fun <T> Observable<T>.onIo(): Disposable {
    return subscribeOn(Schedulers.io())
        .subscribe({}, { it.printStackTrace() })
}