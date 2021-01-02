package com.bytebyte6.view.search


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

import androidx.lifecycle.ViewModel
import com.bytebyte6.base.BaseViewModelDelegate
import com.bytebyte6.base.LoadData
import com.bytebyte6.data.IpTvRepository
import com.bytebyte6.data.model.IpTv

class SearchViewModel(
    private val repository: IpTvRepository,
    private val baseViewModelDelegate: BaseViewModelDelegate
) : ViewModel(),
    BaseViewModelDelegate by baseViewModelDelegate,
    IpTvRepository by repository {

    private val searchLiveData = MutableLiveData<List<IpTv>>()

    private val searchLoadData by lazy {
        object : LoadData<List<IpTv>> {
            override fun start() {

            }

            override fun success(data: List<IpTv>) {
                searchLiveData.postValue(data)
            }

            override fun fail(error: Throwable) {

            }
        }
    }

    fun searchLiveData(): LiveData<List<IpTv>> {
        return searchLiveData
    }

    fun search(key: CharSequence?) {
        if (!key.isNullOrEmpty()) {
            search(key.toString(), loadData = searchLoadData)
        }
    }

    override fun onCleared() {
        dispose()
        super.onCleared()
    }
}

