package com.bytebyte6.viewmodel

import androidx.lifecycle.MutableLiveData
import com.bytebyte6.common.BaseViewModel
import com.bytebyte6.common.Result
import com.bytebyte6.common.onIo
import com.bytebyte6.data.DataManager
import com.bytebyte6.usecase.SearchCountryImageParam
import com.bytebyte6.usecase.SearchCountryImageUseCase

class HomeViewModel(
    dataManager: DataManager,
    private val searchCountryImageUseCase: SearchCountryImageUseCase
) : BaseViewModel() {

    val cs = dataManager.countries()

    val lang = dataManager.allLanguage()

    val category = dataManager.allCategory()

    fun searchLogo(first: Int, last: Int) {
        if (cs.value == null)
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

    fun logoWrong(pos: Int) {
        if (cs.value != null) {
            addDisposable(
                searchCountryImageUseCase.execute(
                    SearchCountryImageParam(pos, 0, cs.value!!, true)
                ).onIo()
            )
        }
    }
}

