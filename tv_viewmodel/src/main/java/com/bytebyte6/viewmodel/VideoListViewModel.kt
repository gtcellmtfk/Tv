package com.bytebyte6.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.bytebyte6.common.*
import com.bytebyte6.data.DataManager
import com.bytebyte6.data.PAGE_SIZE
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

    val itemChanged: LiveData<Int> = searchTvLogoUseCase.itemChanged.map {
        val indexOf = pagingHelper.getList().indexOf(it)
        val pos = if (indexOf == -1) 0 else indexOf
        pos
    }

    private val pagingHelper: PagingHelper<Tv> = object : PagingHelper<Tv>(PAGE_SIZE) {
        override fun count(): Int = dataManager.getFtsTvCount(getKey())
        override fun paging(offset: Int, pageSize: Int): List<Tv> {
            val tvs = dataManager.ftsTvPaging(offset, getKey(), pageSize)
            addDisposable(searchTvLogoUseCase.execute(SearchTvLogoParam(tvs)).onIo())
            return tvs
        }
    }

    private var key: String = ""

    val tvs: LiveData<Result<List<Tv>>> = pagingHelper.result()

    val favoriteResult = favoriteTvUseCase.result()

    fun setKey(key: String) {
        this.key = key
    }

    fun getKey() = key

    fun count(item: String): LiveData<Int> = dataManager.ftsTvCount(item)

    fun loadMore() {
        addDisposable(pagingHelper.loadResult().onSingle())
    }

    fun fav(pos: Int) {
        addDisposable(
            favoriteTvUseCase.execute(UpdateTvParam(pos, pagingHelper.getList()[pos])).onIo()
        )
    }

    private var first = true

    fun first() {
        if (first) {
            loadMore()
            first = false
        }
    }

    override fun onCleared() {
        searchTvLogoUseCase.stop()
        super.onCleared()
    }
}

