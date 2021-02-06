package com.bytebyte6.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.switchMap
import com.bytebyte6.common.BaseViewModel
import com.bytebyte6.common.onIo
import com.bytebyte6.data.DataManager
import com.bytebyte6.data.entity.Tv
import com.bytebyte6.usecase.FavoriteTvUseCase
import com.bytebyte6.usecase.SearchTvLogoParam
import com.bytebyte6.usecase.SearchTvLogoUseCase
import com.bytebyte6.usecase.UpdateTvParam

class SearchViewModel(
    private val dataManager: DataManager,
    private val searchTvLogoUseCase: SearchTvLogoUseCase,
    private val favoriteTvUseCase: FavoriteTvUseCase
) : BaseViewModel(), Observer<List<Tv>> {

    private val keyword = MutableLiveData<String>()

    val searchResult = keyword.switchMap {
        dataManager.tvsByKeyword(it)
    }

    val favoriteResult = favoriteTvUseCase.result()

    val logoUrlSearchResult = searchTvLogoUseCase.result()

    init {
        searchResult.observeForever(this)
    }

    override fun onCleared() {
        searchResult.removeObserver(this)
        super.onCleared()
    }

    fun search(key: CharSequence?) {
        if (!key.isNullOrEmpty()) {
            keyword.value = key.toString()
        }
    }

    fun fav(pos: Int) {
        searchResult.value?.apply {
            val tv = this[pos]
            addDisposable(
                favoriteTvUseCase.execute(UpdateTvParam(pos, tv)).onIo()
            )
        }
    }

    override fun onChanged(list: List<Tv>) {
        if (list.isNotEmpty()) {
            addDisposable(
                searchTvLogoUseCase.execute(SearchTvLogoParam(list)).onIo()
            )
        }
    }
}

