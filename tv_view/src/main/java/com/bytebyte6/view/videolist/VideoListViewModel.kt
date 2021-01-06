package com.bytebyte6.view.videolist

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.bytebyte6.data.dao.TvFtsDao
import com.bytebyte6.data.entity.Tv
import com.bytebyte6.data.entity.TvFts

class VideoListViewModel(
    private val tvFtsDao: TvFtsDao
) : ViewModel(){
    fun search(item: String): LiveData<List<Tv>> = tvFtsDao.searchLiveData(item).map {
        TvFts.toIpTvs(it)
    }
}

