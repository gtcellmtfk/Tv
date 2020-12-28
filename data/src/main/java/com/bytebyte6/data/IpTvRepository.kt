package com.bytebyte6.data

import androidx.lifecycle.LiveData
import com.bytebyte6.base.LoadData
import com.bytebyte6.data.model.IpTv
import com.bytebyte6.data.model.WrapLanguages

interface IpTvRepository {
    fun refresh(loadData: LoadData<List<IpTv>>)
    fun init(loadData: LoadData<List<IpTv>>)
    fun clear()
    fun liveData(): LiveData<List<IpTv>>
    fun nextPage(): LiveData<List<IpTv>>
    fun liveDataByAllCategory(): LiveData<List<String>>
    fun liveDataByAllLanguage(): LiveData<List<WrapLanguages>>
    fun liveDataByAllCountry(): LiveData<List<String>>
    fun liveDataByCategory(category: String): LiveData<List<IpTv>>
    fun liveDataByLanguage(language: String): LiveData<List<IpTv>>
    fun liveDataByCountry(countryName: String): LiveData<List<IpTv>>
}

