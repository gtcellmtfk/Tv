package com.bytebyte6.data

import android.os.Parcelable
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.bytebyte6.base.Event
import com.bytebyte6.base.EventLiveData
import com.bytebyte6.base.mvi.Result
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

abstract class PagingHelper<T> {

    private val liveData = EventLiveData<Result<List<T>>>()
    private var page = 0
    private var list = mutableListOf<T>()

    fun liveData(): LiveData<Event<Result<List<T>>>> = liveData.liveData()

    fun getPage() = page

    fun getCurrentSize(): Int = list.size

    fun loadData() {
        try {
            liveData.postEventValue(Result.Loading)
            if (list.size < count()) {
                list.addAll(paging(offset = page * PAGE_SIZE))
                page++
                liveData.postEventValue(Result.Success(list, list.size >= count()))
            } else {
                liveData.postEventValue(Result.Success(emptyList(), true))
            }
        } catch (e: Exception) {
            liveData.postEventValue(Result.Error(e))
        }
    }

    fun refresh() {
        page = 0
        list.clear()
        list = mutableListOf()
        loadData()
    }

    @WorkerThread
    abstract fun count(): Int

    @WorkerThread
    abstract fun paging(offset: Int): List<T>
}