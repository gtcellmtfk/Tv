package com.bytebyte6.view.home

import androidx.lifecycle.MutableLiveData
import com.bytebyte6.base.Result
import com.bytebyte6.base.BaseViewModel
import com.bytebyte6.data.dao.CountryDao
import com.bytebyte6.data.dao.TvDao
import com.bytebyte6.base.onIo
import com.bytebyte6.usecase.CountryImageSearchUseCase
import com.bytebyte6.usecase.TvRefreshUseCase

class HomeViewModel(
    tvDao: TvDao,
    countryDao: CountryDao,
    private val tvRefreshUseCase: TvRefreshUseCase,
    private val countryImageSearchUseCase: CountryImageSearchUseCase
) : BaseViewModel() {
    val tvRefresh = MutableLiveData<Result<Boolean>>()

    val cs = countryDao.countries()

    val lang = tvDao.allLanguage()

    val category = tvDao.allCategory()

    fun refresh() {
        addDisposable(
            tvRefreshUseCase.getSingle()
                .doOnSuccess { tvRefresh.postValue(Result.Success(true)) }
                .doOnError { tvRefresh.postValue(Result.Error(it)) }
                .doOnSubscribe { tvRefresh.postValue(Result.Loading()) }
                .onIo()
        )
    }

    fun searchLogo(pos: Int) {
        val country = cs.value?.get(pos) ?: return
        addDisposable(
            countryImageSearchUseCase.execute(country).onIo()
        )
    }
}

