package com.bytebyte6.viewmodel

import com.bytebyte6.common.BaseViewModel
import com.bytebyte6.common.onIo
import com.bytebyte6.data.DataManager
import com.bytebyte6.data.entity.Country
import com.bytebyte6.usecase.ChangeCountryImageUseCase
import com.bytebyte6.usecase.CountryParam

class HomeViewModel(
    dataManager: DataManager,
    private val changeCountryImageUseCase: ChangeCountryImageUseCase
) : BaseViewModel() {

    val cs = dataManager.countries()

    val lang = dataManager.allLanguage()

    val category = dataManager.allCategory()

    val result=changeCountryImageUseCase.result()

    fun changeImage(country: Country,pos:Int) {
        addDisposable(
            changeCountryImageUseCase.execute(
                CountryParam(country,pos)
            ).onIo()
        )
    }
}

