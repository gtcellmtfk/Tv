package com.bytebyte6.viewmodel

import androidx.lifecycle.LiveData
import com.bytebyte6.common.*
import com.bytebyte6.data.DataManager
import com.bytebyte6.data.entity.Tv
import com.bytebyte6.usecase.FavoriteTvUseCase
import com.bytebyte6.usecase.SearchTvLogoParam
import com.bytebyte6.usecase.SearchTvLogoUseCase
import com.bytebyte6.usecase.UpdateTvParam

class VideoListViewModel(
    private val dataManager: DataManager,
    private val searchTvLogoUseCase: SearchTvLogoUseCase,
    private val favoriteTvUseCase: FavoriteTvUseCase
) : BaseViewModel() {

    private val pagingHelper: PagingHelper<Tv>  = object : PagingHelper<Tv>() {
        override fun count(): Int = dataManager.getFtsTvCount(getKey())
        override fun paging(offset: Int, pageSize: Int): List<Tv> {
            val tvs = dataManager.ftsTvPaging(offset, getKey(), pageSize)
            addDisposable(searchTvLogoUseCase.execute(SearchTvLogoParam(tvs)).onSingle())
            return tvs
        }
    }

    private var key: String = ""

    val tvs: LiveData<Result<List<Tv>>>  = pagingHelper.result()

    val favoriteResult = favoriteTvUseCase.result()

    val logoSearchResult = searchTvLogoUseCase.result()

    fun setKey(key: String) {
        this.key = key
    }

    fun getKey() = key

    fun count(item: String): LiveData<Int> = dataManager.ftsTvCount(item)

    fun loadMore() {
        addDisposable(pagingHelper.loadResult().onIo())
    }

    private var first = true

    fun loadOnce() {
        if (first) {
            first = false
            loadMore()
        }
    }

    fun fav(pos: Int) {
        addDisposable(
            favoriteTvUseCase.execute(UpdateTvParam(pos, pagingHelper.getList()[pos])).onIo()
        )
    }

    override fun onCleared() {
        super.onCleared()
        searchTvLogoUseCase.stop()
    }
}

