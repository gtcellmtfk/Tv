package com.bytebyte6.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bytebyte6.base.mvi.Result
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

abstract class RxSingleUseCase<Param, ResultType> {

    private val result: MutableLiveData<Result<ResultType>> = MutableLiveData()

    fun result(): LiveData<Result<ResultType>> = result

    fun execute(param: Param): Single<ResultType> = Single.create<ResultType> {
        val result = doSomething(param)
        it.onSuccess(result)
    }
        .doOnSubscribe { result.postValue((Result.Loading())) }
        .doOnSuccess {
            result.postValue((Result.Success(it)))
        }
        .doOnError {
            it.printStackTrace()
            result.postValue((Result.Error(it)))
        }

    abstract fun doSomething(param: Param): ResultType
}

fun <T> Single<T>.onIo(): Disposable {
    return subscribeOn(Schedulers.io())
        .subscribe()
}