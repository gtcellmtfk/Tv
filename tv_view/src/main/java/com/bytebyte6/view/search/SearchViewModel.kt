package com.bytebyte6.view.search


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

import androidx.lifecycle.ViewModel
import com.bytebyte6.base.BaseViewModelDelegate
import com.bytebyte6.base.LoadData
import com.bytebyte6.data.TvRepository
import com.bytebyte6.data.entity.Tv

class SearchViewModel(
    private val repository: TvRepository,
    private val baseViewModelDelegate: BaseViewModelDelegate
) : ViewModel(),
    BaseViewModelDelegate by baseViewModelDelegate,
    TvRepository by repository {

    private val searchLiveData = MutableLiveData<List<Tv>>()

    private val searchLoadData by lazy {
        object : LoadData<List<Tv>> {
            override fun start() {

            }

            override fun success(data: List<Tv>) {
                searchLiveData.postValue(data)
            }

            override fun fail(error: Throwable) {

            }
        }
    }

    fun searchLiveData(): LiveData<List<Tv>> {
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

