package com.bytebyte6.view.home

import android.util.ArrayMap
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.bytebyte6.data.dao.CountryDao
import com.bytebyte6.data.dao.TvDao
import com.bytebyte6.view.TAB_CATEGORY
import com.bytebyte6.view.TAB_COUNTRY
import com.bytebyte6.view.TAB_LANGUAGE
import com.bytebyte6.view.usecase.TvRefreshUseCase
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlin.collections.set

class HomeViewModel(
    private val tvDao: TvDao,
    private val countryDao: CountryDao,
    private val tvRefreshUseCase: TvRefreshUseCase
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val tvRefresh = tvRefreshUseCase.eventLiveData()

    val cs = countryDao.countries()

    val lang=tvDao.liveDataByAllLanguage()

    val category=tvDao.liveDataByAllCategory()

    fun refresh() {
        compositeDisposable.add(
            tvRefreshUseCase.execute("从网络下载最新数据，插入到本地数据库")
        )
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }
}

