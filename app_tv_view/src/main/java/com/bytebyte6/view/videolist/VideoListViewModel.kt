package com.bytebyte6.view.videolist

import androidx.lifecycle.LiveData
import com.bytebyte6.base.mvi.Result
import com.bytebyte6.base_ui.BaseViewModel
import com.bytebyte6.base.PagingHelper
import com.bytebyte6.data.dao.TvFtsDao
import com.bytebyte6.data.entity.TvFts
import com.bytebyte6.base.onIo
import com.bytebyte6.view.usecase.TvLogoSearchUseCase

class VideoListViewModel(
    private val tvFtsDao: TvFtsDao,
    private val tvLogoSearchUseCase: TvLogoSearchUseCase
) : BaseViewModel() {

    private val pagingHelper: PagingHelper<TvFts> = object : PagingHelper<TvFts>() {
        override fun count(): Int = tvFtsDao.getCount(getKey())

        override fun paging(offset: Int): List<TvFts> = tvFtsDao.paging(offset, getKey())
    }

    private var key: String = ""

    val tvs: LiveData<Result<List<TvFts>>> = pagingHelper.result()

    init {
        loadMore()
    }

    fun setKey(key: String) {
        this.key = key
    }

    fun getKey() = key


    fun count(item: String): LiveData<Int> = tvFtsDao.count(item)

    fun loadMore() {
        addDisposable(
            pagingHelper.loadData().onIo()
        )
    }

    fun searchLogo(pos: Int) {
        val tvId = pagingHelper.getList()[pos].tvId
        addDisposable(
            tvLogoSearchUseCase.execute(tvId).onIo()
        )
    }
}

