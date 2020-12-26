package com.bytebyte6.data

import androidx.lifecycle.LiveData
import com.bytebyte6.data.model.IpTv

interface IpTvRepository {
    fun refresh(loadData: LoadData<List<IpTv>>)
    fun init(loadData: LoadData<List<IpTv>>)
    fun clear()
    fun liveData(): LiveData<List<IpTv>>
    fun nextPage(): LiveData<List<IpTv>>
}

