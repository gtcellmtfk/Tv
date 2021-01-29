package com.bytebyte6.base

import android.os.Parcelable
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.core.Single
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PageResponse<T : Parcelable>(
    var page: Int = 0,
    var pageCount: Int = 0,
    var count: Int = 0,
    var pageSize: Int = 0,
    var list: List<T> = emptyList()
) : Parcelable

abstract class NetWorkPagingHelper<T : Parcelable> : PagingHelper<T>() {

    private var pageResponse: PageResponse<T>? = null

    fun init() {
        if (pageResponse == null) {
            pageResponse = loadNetWorkData()
        }
    }

    private fun loadNetWorkData(): PageResponse<T> {
        return PageResponse()
    }

    override fun count(): Int = if (pageResponse == null) 0 else pageResponse!!.count
}

abstract class PagingHelper<T>(private val pageSize: Int = 20) {

    private val result = MutableLiveData<Result<List<T>>>()
    private var page = 0
    private var list = mutableListOf<T>()

    fun result(): LiveData<Result<List<T>>> = result

    fun getPage() = page

    fun getCurrentSize(): Int = list.size

    fun getList() = list

    /**
     * 数据已被更改,重新发出值
     */
    fun dataHasBeenChanged() {
        result.postValue(Result.Success(list))
    }

    fun loadData(): Single<Result<List<T>>> {
        return Single.create<Result<List<T>>> {
            if (list.size < count()) {
                result.postValue((Result.Loading()))
                list.addAll(paging(offset = page * pageSize))
                page++
                it.onSuccess(Result.Success(list, list.size >= count()))
            } else {
                it.onSuccess((Result.Success(emptyList(), true)))
            }
        }
            .doOnSuccess { result.postValue(it) }
            .doOnError { result.postValue(Result.Error(it)) }
    }

    fun refresh(): Single<Result<List<T>>> {
        page = 0
        list.clear()
        list = mutableListOf()
        return loadData()
    }

    @WorkerThread
    abstract fun count(): Int

    @WorkerThread
    abstract fun paging(offset: Int): List<T>
}