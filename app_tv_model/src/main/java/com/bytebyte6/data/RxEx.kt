package com.bytebyte6.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bytebyte6.base.mvi.Result
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

abstract class RxSingleUseCase<P, R> {

    private val result: MutableLiveData<Result<R>> = MutableLiveData()

    fun result(): LiveData<Result<R>> = result

    fun execute(param: P): Disposable = getSingle(param)
        .doOnSubscribe { result.postValue((Result.Loading())) }
        .subscribeOn(Schedulers.io())
        .subscribe({
            result.postValue((Result.Success(it)))
        }, {
            it.printStackTrace()
            result.postValue((Result.Error(it)))
        })

    abstract fun getSingle(param: P): Single<R>
}