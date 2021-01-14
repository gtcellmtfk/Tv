package com.bytebyte6.view.home

import com.bytebyte6.base_ui.BaseViewModel
import com.bytebyte6.data.dao.CountryDao
import com.bytebyte6.data.dao.TvDao
import com.bytebyte6.view.usecase.CountryImageSearchUseCase
import com.bytebyte6.view.usecase.TvRefreshUseCase

class HomeViewModel(
    tvDao: TvDao,
    countryDao: CountryDao,
    private val tvRefreshUseCase: TvRefreshUseCase,
    private val countryImageSearchUseCase: CountryImageSearchUseCase
) : BaseViewModel() {
    val tvRefresh = tvRefreshUseCase.result()

    val cs = countryDao.countries()

    val lang = tvDao.allLanguage()

    val category = tvDao.allCategory()

    fun refresh() {
        addDisposable(
            tvRefreshUseCase.execute("")
        )
    }

    fun searchLogo(pos: Int) {
        val country = cs.value?.get(pos) ?: return
        addDisposable(
            countryImageSearchUseCase.execute(country)
        )
    }
}

