package com.bytebyte6.viewmodel

import androidx.lifecycle.MutableLiveData
import com.bytebyte6.common.BaseViewModel
import com.bytebyte6.common.PagingHelper
import com.bytebyte6.common.onIo
import com.bytebyte6.common.onSingle
import com.bytebyte6.data.DataManager
import com.bytebyte6.data.PAGE_SIZE
import com.bytebyte6.data.entity.Tv
import com.bytebyte6.usecase.*

class PlaylistViewModel(
    private val searchTvLogoUseCase: SearchTvLogoUseCase,
    private val favoriteTvUseCase: FavoriteTvUseCase,
    private val dataManager: DataManager
) : BaseViewModel() {

    val count = MutableLiveData<Int>()

    private val pagingHelper = object : PagingHelper<Tv>(PAGE_SIZE) {
        override fun count(): Int {
            val tvCount = dataManager.getTvCountByPlaylistId(playlistId)
            count.postValue(tvCount)
            return tvCount
        }

        override fun paging(offset: Int, pageSize: Int): List<Tv> {
            val page = getPage()
            val tvs = dataManager.getTvsByPlaylistId(playlistId, page)
            addDisposable(
                searchTvLogoUseCase.execute(SearchTvLogoParam(tvs))
                    .doOnSuccess { dataManager.updatePlaylistCache(playlistId, tvs, page) }
                    .onIo()
            )
            return tvs
        }
    }

    val tvs = pagingHelper.result()

    var playlistId: Long = 0

    val downloadResult = favoriteTvUseCase.result()

    fun fav(pos: Int, tv: Tv) {
        addDisposable(
            favoriteTvUseCase.execute(UpdateTvParam(pos,tv)).onIo()
        )
    }

    fun loadMore() {
        addDisposable(pagingHelper.loadResult().onSingle())
    }

    private var first = true

    fun first() {
        if (first) {
            loadMore()
            first = false
        }
    }
}
