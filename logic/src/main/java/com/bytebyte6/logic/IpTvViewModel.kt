package com.bytebyte6.logic

import android.content.Context
import android.util.ArrayMap
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.bytebyte6.base.BaseViewModelDelegate
import com.bytebyte6.data.IpTvRepository
import com.bytebyte6.data.model.Category
import com.bytebyte6.data.model.Country
import com.bytebyte6.data.model.IpTv
import com.bytebyte6.data.model.Languages

class IpTvViewModel(
    private val repository: IpTvRepository,
    private val baseViewModelDelegate: BaseViewModelDelegate,
    private val context: Context
) : ViewModel(), BaseViewModelDelegate by baseViewModelDelegate {

    var tab: Int = 0

    var clickItem: Any = Any()

    private val map = ArrayMap<Int, LiveData<*>>(3)

    init {
        repository.init(getDefaultLoadData())
        map[TAB_COUNTRY] = repository.liveDataByAllCountry()
        map[TAB_LANGUAGE] = repository.liveDataByAllLanguage()
        map[TAB_CATEGORY] = repository.liveDataByAllCategory()
    }

    fun getTitle(): String {
        return when (clickItem) {
            is Country -> {
                (clickItem as Country).countryName
            }
            is Category -> {
                (clickItem as Category).category
            }
            is Languages -> {
                (clickItem as Languages).getString()
            }
            else -> ""
        }
    }

    /**
     * ViewPager展示的内容
     * @return LiveData<List<Any>>
     */
    fun listLiveData(tab: Int): LiveData<*>? {
        return map[tab]
    }

    /**
     * VideoDialog展示的内容
     */
    fun ipTvsLiveData(): LiveData<List<IpTv>>? {
        return when (tab) {
            TAB_COUNTRY -> {
                val country = clickItem as Country
                repository.liveDataByCountry(country.countryName)
            }
            TAB_LANGUAGE -> {
                repository.liveDataByLanguage(((clickItem as Languages)))
            }
            TAB_CATEGORY -> {
                val category = clickItem as Category
                repository.liveDataByCategory(category.category)
            }
            else -> null
        }
    }

    override fun onCleared() {
        repository.dispose()
        super.onCleared()
    }

    fun refresh() {
        repository.refresh(getDefaultLoadData())
    }
}

