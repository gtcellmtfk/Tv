package com.bytebyte6.view

import android.util.ArrayMap
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bytebyte6.base.BaseViewModelDelegate
import com.bytebyte6.data.TvRepository
import com.bytebyte6.data.entity.Tv
import com.bytebyte6.data.model.Category
import com.bytebyte6.data.model.Country
import com.bytebyte6.data.model.Languages
import io.reactivex.rxjava3.disposables.CompositeDisposable

class TvViewModel(
    private val repository: TvRepository,
    private val baseViewModelDelegate: BaseViewModelDelegate
) : ViewModel(),
    BaseViewModelDelegate by baseViewModelDelegate,
    TvRepository by repository {

    var tab: Int = 0

    var clickItem: String = ""

    private val map = ArrayMap<Int, LiveData<List<String>>>(3)

    init {
        init(getDefaultLoadData())
        map[TAB_COUNTRY] =
            Transformations.map<List<Country>, List<String>>(liveDataByAllCountry()) {
                val list = mutableListOf<String>()
                for (country in it) {
                    list.add(country.countryName)
                }
                list
            }
        map[TAB_LANGUAGE] =
            Transformations.map<List<Languages>, List<String>>(liveDataByAllLanguage()) {
                val list = mutableListOf<String>()
                for (languages in it) {
                    val temp = languages.getString()
                    if (temp.isNotEmpty()) {
                        list.add(temp)
                    }
                }
                list
            }
        map[TAB_CATEGORY] =
            Transformations.map<List<Category>, List<String>>(liveDataByAllCategory()) {
                val list = mutableListOf<String>()
                for (category in it) {
                    if (category.category.isNotEmpty()) {
                        list.add(category.category)
                    }
                }
                list
            }
    }

    fun getTitle(): String {
        return clickItem
    }

    /**
     * ViewPager展示的内容
     * @return LiveData<List<Any>>
     */
    fun listLiveData(tab: Int): LiveData<List<String>>? {
        return map[tab]
    }

    /**
     * VideoDialog
     * VideoListFragment
     * 展示的内容
     */
    fun ipTvsLiveData(): LiveData<List<Tv>> = search(clickItem)

    override fun onCleared() {
        dispose()
        super.onCleared()
    }

    fun refresh() {
        refresh(getDefaultLoadData())
    }
}

