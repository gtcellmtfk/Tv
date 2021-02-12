package com.bytebyte6.viewmodel

import androidx.lifecycle.MutableLiveData
import com.bytebyte6.common.BaseViewModel
import com.bytebyte6.common.Result
import com.bytebyte6.common.onIo
import com.bytebyte6.data.DataManager
import com.bytebyte6.usecase.SearchCountryImageParam
import com.bytebyte6.usecase.SearchCountryImageUseCase
import com.bytebyte6.usecase.TvRefreshUseCase

class HomeViewModel(
    dataManager: DataManager,
    private val tvRefreshUseCase: TvRefreshUseCase,
    private val searchCountryImageUseCase: SearchCountryImageUseCase
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

    fun searchLogo(first: Int, last: Int) {
        if (cs.value==null)
            return
        addDisposable(
            searchCountryImageUseCase.execute(
                SearchCountryImageParam(
                    first,
                    last,
                    cs.value!!
                )
            ).onIo()
        )
    }

    private var first = true

    fun searchLogoOnce() {
        if (cs.value == null)
            return
        if (first) {
            addDisposable(
                searchCountryImageUseCase.execute(
                    SearchCountryImageParam(
                        0,
                        10,
                        cs.value!!
                    )
                ).onIo()
            )
            first = false
        }
    }
}

