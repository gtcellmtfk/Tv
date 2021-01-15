package com.bytebyte6.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bytebyte6.base.mvi.Result
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

abstract class RxSingleUseCase<Param, R> {

    private val result: MutableLiveData<Result<R>> = MutableLiveData()

    fun result(): LiveData<Result<R>> = result

    fun execute(param: Param): Single<R> = Single.create<R> {
        try {
            val r = doSomething(param)
            it.onSuccess(r)
        } catch (e: Exception) {
            it.onError(e)
        }
    }
        .doOnSubscribe { result.postValue((Result.Loading())) }
        .doOnSuccess {
            result.postValue((Result.Success(it)))
        }
        .doOnError {
            it.printStackTrace()
            result.postValue((Result.Error(it)))
        }

    abstract fun doSomething(param: Param): R
}

fun <T> Single<T>.onIo(): Disposable {
    return subscribeOn(Schedulers.io())
        .subscribe()
}