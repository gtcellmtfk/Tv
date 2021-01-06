package com.bytebyte6.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bytebyte6.base.mvi.Result
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

abstract class RxUseCase<P, R> {
    private val liveData: MutableLiveData<Result<R>> = MutableLiveData()
    fun liveData(): LiveData<Result<R>> = liveData
    fun execute(param: P): Disposable = getSingle(param)
        .doOnSubscribe { liveData.postValue(Result.Loading) }
        .subscribeOn(Schedulers.io())
        .subscribe({
            liveData.postValue(Result.Success(it))
        }, {
            it.printStackTrace()
            liveData.postValue(Result.Error(it))
        })

    abstract fun getSingle(param: P): Single<R>
}