package com.bytebyte6.data

import androidx.lifecycle.LiveData
import com.bytebyte6.base.LoadData
import com.bytebyte6.data.model.*
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single

interface IpTvRepository {
    fun refresh(loadData: LoadData<List<IpTv>>)
    fun init(loadData: LoadData<IpTv>)
    fun dispose()
    fun liveDataByAllCategory(): LiveData<List<Category>>
    fun liveDataByAllLanguage(): LiveData<List<Languages>>
    fun liveDataByAllCountry(): LiveData<List<Country>>
    fun search(key: String,loadData: LoadData<List<IpTv>>)
    fun search(key: String):LiveData<List<IpTv>>
}

