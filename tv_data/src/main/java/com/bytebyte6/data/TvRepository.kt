package com.bytebyte6.data

import androidx.lifecycle.LiveData
import com.bytebyte6.base.LoadData
import com.bytebyte6.data.entity.Tv
import com.bytebyte6.data.model.*

interface TvRepository {
    fun refresh(loadData: LoadData<List<Tv>>)
    fun init(loadData: LoadData<Int>)
    fun dispose()
    fun liveDataByAllCategory(): LiveData<List<Category>>
    fun liveDataByAllLanguage(): LiveData<List<Languages>>
    fun liveDataByAllCountry(): LiveData<List<Country>>
    fun search(key: String,loadData: LoadData<List<Tv>>)
    fun search(key: String):LiveData<List<Tv>>
}

