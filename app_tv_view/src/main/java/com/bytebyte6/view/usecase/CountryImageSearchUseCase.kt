package com.bytebyte6.view.usecase

import com.bytebyte6.base.RxSingleUseCase
import com.bytebyte6.data.dao.CountryDao
import com.bytebyte6.data.entity.Country
import com.bytebyte6.data.work.ImageSearch

class CountryImageSearchUseCase(
    private val imageSearch: ImageSearch,
    private val countryDao: CountryDao
) : RxSingleUseCase<Country, Boolean>() {

    override fun doSomething(param: Country): Boolean {
        if (param.images.isEmpty() && param.name.isNotEmpty()) {
            val images = imageSearch.search(
                param.name
                    .replace(" ", "+")
                    .plus("+flag+国旗")
            )
            param.images = images
            countryDao.insert(param)
        }
        return true
    }
}