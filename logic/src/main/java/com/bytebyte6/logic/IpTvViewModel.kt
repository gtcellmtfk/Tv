package com.bytebyte6.logic

import androidx.lifecycle.LiveData
import com.bytebyte6.base.BaseViewModel
import com.bytebyte6.data.Converter
import com.bytebyte6.data.IpTvRepository
import com.bytebyte6.data.model.IpTv
import com.bytebyte6.data.model.WrapLanguages

class IpTvViewModel(private val repository: IpTvRepository, private val converter: Converter) :
    BaseViewModel() {

    /**
     * ViewPager展示的内容
     */
    fun listLiveData(tab: Int): LiveData<*>? {
        return when (tab) {
            TAB_COUNTRY -> repository.liveDataByAllCountry()
            TAB_LANGUAGE -> repository.liveDataByAllLanguage()
            TAB_CATEGORY -> repository.liveDataByAllCategory()
            else -> null
        }
    }

    /**
     * VideoDialog展示的内容
     */
    fun ipTvsLiveData(tab: Int, clickItem: Any): LiveData<List<IpTv>>? {
        return when (tab) {
            TAB_COUNTRY -> repository.liveDataByCountry(clickItem.toString())
            TAB_LANGUAGE -> repository.liveDataByLanguage(converter.toJson((clickItem as WrapLanguages).language))
            TAB_CATEGORY -> repository.liveDataByCategory(clickItem.toString())
            else -> null
        }
    }

    /**
     * 分页查询
     */
    fun nextPage(): LiveData<List<IpTv>> {
        return repository.nextPage()
    }

    override fun onCleared() {
        repository.clear()
        super.onCleared()
    }

    init {
        repository.init(getDefaultLoadData())
    }
}

