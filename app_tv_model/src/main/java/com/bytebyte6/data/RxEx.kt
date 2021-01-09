package com.bytebyte6.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bytebyte6.base.Event
import com.bytebyte6.base.mvi.Result
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

abstract class RxSingleUseCase<P, R> {

    /**
     * 一次性事件
     */
    private val liveData: MutableLiveData<Event<Result<R>>> = MutableLiveData()

    fun eventLiveData(): LiveData<Event<Result<R>>> = liveData

    fun execute(param: P): Disposable = getSingle(param)
        .doOnSubscribe { liveData.postValue(Event(Result.Loading)) }
        .subscribeOn(Schedulers.io())
        .subscribe({
            liveData.postValue(Event(Result.Success(it)))
        }, {
            it.printStackTrace()
            liveData.postValue(Event(Result.Error(it)))
        })

    abstract fun getSingle(param: P): Single<R>
}