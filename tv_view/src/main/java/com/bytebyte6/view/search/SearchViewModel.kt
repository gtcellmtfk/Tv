package com.bytebyte6.view.search

import androidx.lifecycle.ViewModel
import com.bytebyte6.view.usecase.SearchTvUseCase
import io.reactivex.rxjava3.disposables.CompositeDisposable


class SearchViewModel(
    private val searchTvUseCase: SearchTvUseCase
) : ViewModel() {

    val tvs = searchTvUseCase.eventLiveData()

    private val compositeDisposable = CompositeDisposable()

    fun search(key: CharSequence?) {
        if (!key.isNullOrEmpty()) {
            compositeDisposable.add(
                searchTvUseCase.execute(key.toString())
            )
        }
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }
}

