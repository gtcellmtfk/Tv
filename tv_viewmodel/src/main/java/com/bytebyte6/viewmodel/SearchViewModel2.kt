package com.bytebyte6.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bytebyte6.common.*
import com.bytebyte6.data.DataManager
import com.bytebyte6.data.PAGE_SIZE
import com.bytebyte6.data.entity.Tv
import com.bytebyte6.usecase.FavoriteTvUseCase
import com.bytebyte6.usecase.SearchTvLogoParam
import com.bytebyte6.usecase.SearchTvLogoUseCase
import com.bytebyte6.usecase.UpdateTvParam

class SearchViewModel2(
    private val dataManager: DataManager,
    private val searchTvLogoUseCase: SearchTvLogoUseCase,
    private val favoriteTvUseCase: FavoriteTvUseCase
) : BaseViewModel() {

    private val count = MutableLiveData(0)

    private val pagingHelper: PagingHelper<Tv> = object : PagingHelper<Tv>(PAGE_SIZE) {
        override fun count(): Int {
            val tvCount = dataManager.getFtsTvCount(getKey())
            count.postValue(tvCount)
            return tvCount
        }

        override fun paging(offset: Int, pageSize: Int): List<Tv> {
            val ftsTvPaging = dataManager.ftsTvPaging(offset, getKey(), pageSize)
            val param = SearchTvLogoParam(ftsTvPaging)
            addDisposable(searchTvLogoUseCase.execute(param).onIo())
            return ftsTvPaging
        }
    }

    private var keyword = ""

    val resultCount: LiveData<Int> = count

    val searchResult = pagingHelper.result()

    val favoriteResult = favoriteTvUseCase.result()

    fun getKey() = keyword

    fun search(key: CharSequence?) {
        if (!key.isNullOrEmpty()) {
            keyword = key.toString()
            addDisposable(pagingHelper.refresh().onSingle())
        } else {
            count.postValue(0)
        }
    }

    fun loadMore() {
        if (getKey().isNotEmpty()) {
            addDisposable(pagingHelper.loadResult().onSingle())
        }
    }

    fun fav(pos: Int) {
        searchResult.getSuccessData()?.apply {
            val tv = this[pos]
            addDisposable(
                favoriteTvUseCase.execute(UpdateTvParam(pos, tv)).onIo()
            )
        }
    }

    override fun onCleared() {
        searchTvLogoUseCase.stop()
        super.onCleared()
    }
}

