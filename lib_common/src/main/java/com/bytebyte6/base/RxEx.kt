package com.bytebyte6.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

abstract class RxUseCase<Param, ResultType> {

    private var data: ResultType? = null

    private val result: MutableLiveData<Result<ResultType>> = MutableLiveData()

    fun postResultAgain() {
        data?.apply {
            result.postValue(Result.Success(this))
        }
    }

    fun getSuccessData() = data

    fun result(): LiveData<Result<ResultType>> = result

    fun execute(param: Param): Single<ResultType> = Single.create<ResultType> {
        val result = run(param)
        it.onSuccess(result)
    }
        .doOnSubscribe { result.postValue((Result.Loading())) }
        .doOnSuccess {
            data = it
            result.postValue((Result.Success(it)))
        }
        .doOnError {
            it.printStackTrace()
            result.postValue((Result.Error(it)))
        }

    fun interval(param: Param, period: Long = 2): Observable<Long> =
        Observable.interval(period, TimeUnit.SECONDS)
            .doOnNext {
                data = run(param)
                result.postValue((Result.Success(data!!)))
            }.doOnError {
                it.printStackTrace()
                result.postValue((Result.Error(it)))
            }

    abstract fun run(param: Param): ResultType
}

fun <T> Single<T>.onIo(): Disposable {
    return subscribeOn(Schedulers.io())
        .subscribe()
}

fun <T> Observable<T>.onIo(): Disposable {
    return subscribeOn(Schedulers.io())
        .subscribe()
}