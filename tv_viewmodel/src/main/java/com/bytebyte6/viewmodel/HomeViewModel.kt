package com.bytebyte6.viewmodel

import androidx.lifecycle.MutableLiveData
import com.bytebyte6.common.Result
import com.bytebyte6.common.BaseViewModel
import com.bytebyte6.common.onIo
import com.bytebyte6.data.DataManager
import com.bytebyte6.usecase.CountryImageSearchUseCase
import com.bytebyte6.usecase.TvRefreshUseCase

class HomeViewModel(
    dataManager: DataManager,
    private val tvRefreshUseCase: TvRefreshUseCase,
    private val countryImageSearchUseCase: CountryImageSearchUseCase
) : BaseViewModel() {
    val tvRefresh = MutableLiveData<Result<Boolean>>()

    val cs = dataManager.countries()

    val lang = dataManager.allLanguage()

    val category = dataManager.allCategory()

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

