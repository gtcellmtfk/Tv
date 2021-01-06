package com.bytebyte6.view.home

import android.util.ArrayMap
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.bytebyte6.data.dao.TvDao
import com.bytebyte6.view.TAB_CATEGORY
import com.bytebyte6.view.TAB_COUNTRY
import com.bytebyte6.view.TAB_LANGUAGE
import com.bytebyte6.view.usecase.CallTvApiUseCase
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlin.collections.set

class HomeViewModel(
    private val tvDao: TvDao,
    private val callTvApiUseCase: CallTvApiUseCase
) : ViewModel() {

    /**
     * tab badge
     */
    private val map = ArrayMap<Int, LiveData<List<String>>>(3)

    private val compositeDisposable = CompositeDisposable()

    val callTvApi = callTvApiUseCase.liveData()

    init {
        map[TAB_COUNTRY] = tvDao.liveDataByAllCountry()

        map[TAB_LANGUAGE] = tvDao.liveDataByAllLanguage().map { list ->
            val names = mutableListOf<String>()
            list.forEach {
                names.add(it.getString())
            }
            names.toList()
        }

        map[TAB_CATEGORY] = tvDao.liveDataByAllCategory()
    }

    fun listLiveData(tab: Int): LiveData<List<String>>? {
        return map[tab]
    }

    fun refresh() {
        compositeDisposable.add(
            callTvApiUseCase.execute("")
        )
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }
}

