package com.bytebyte6.view.videolist

import androidx.lifecycle.LiveData
import com.bytebyte6.base_ui.BaseViewModel
import com.bytebyte6.base.Event
import com.bytebyte6.base.mvi.Result
import com.bytebyte6.data.PagingHelper
import com.bytebyte6.data.dao.CountryDao
import com.bytebyte6.data.dao.TvFtsDao
import com.bytebyte6.data.entity.TvFts

class VideoListViewModel(
    private val tvFtsDao: TvFtsDao,
    private val countryDao: CountryDao
) : BaseViewModel() {

    private lateinit var pagingHelper:PagingHelper<TvFts>

    fun search(item: String): LiveData<Int> = tvFtsDao.countLiveData(item)

    fun loadMore() {
        pagingHelper.loadData()
    }

    fun tvs(key:String):LiveData<Event<Result<List<TvFts>>>>{
        pagingHelper = object : PagingHelper<TvFts>() {
            override fun count(): Int = tvFtsDao.count(key)

            override fun paging(offset: Int): List<TvFts> = tvFtsDao.paging(offset, key)
        }
        return pagingHelper.result()
    }
}

