package com.bytebyte6.common

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.core.Single

object NoMoreData : Throwable("No More Data!!!")

abstract class PagingHelper<T>(private val pageSize: Int = 20) {

    private val result = MutableLiveData<Result<List<T>>>()
    private var page = 0
    private var list = mutableListOf<T>()
    private var end = false

    fun result(): LiveData<Result<List<T>>> = result

    fun getPage() = page

    fun getCurrentSize(): Int = list.size

    fun getList() = list

    /**
     * 数据已被更改,重新发出值
     */
    fun dataHasBeenChanged() {
        result.postValue(Result.Success(list, end))
    }

    fun loadResult(): Single<Result<List<T>>> {
        return Single.create<Result<List<T>>> {
            if (end) {
                throw NoMoreData
            } else {
                result.postValue((Result.Loading()))
                list.addAll(paging(offset = page * pageSize, pageSize = pageSize))
                page++
                end = list.size >= count()
                it.onSuccess(Result.Success(list, end))
            }
        }
            .doOnSuccess { result.postValue(it) }
            .doOnError { result.postValue(Result.Error(it)) }
    }

    fun refresh(): Single<Result<List<T>>> {
        end = false
        page = 0
        list.clear()
        list = mutableListOf()
        return loadResult()
    }

    @WorkerThread
    abstract fun count(): Int

    @WorkerThread
    abstract fun paging(offset: Int, pageSize: Int = 20): List<T>
}