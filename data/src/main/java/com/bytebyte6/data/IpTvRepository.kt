package com.bytebyte6.data

import androidx.lifecycle.LiveData
import com.bytebyte6.base.LoadData
import com.bytebyte6.data.model.*

interface IpTvRepository {
    fun refresh(loadData: LoadData<List<IpTv>>)
    fun init(loadData: LoadData<IpTv>)
    fun dispose()
    fun liveDataByAllCategory(): LiveData<List<Category>>
    fun liveDataByAllLanguage(): LiveData<List<Languages>>
    fun liveDataByAllCountry(): LiveData<List<Country>>
    fun liveDataByCategory(category: String): LiveData<List<IpTv>>
    fun liveDataByLanguage(languages: Languages): LiveData<List<IpTv>>
    fun liveDataByCountry(countryName: String): LiveData<List<IpTv>>
}

