package com.bytebyte6.usecase

import com.bytebyte6.base.RxSingleUseCase
import com.bytebyte6.data.dao.CountryDao
import com.bytebyte6.data.entity.Country
import com.bytebyte6.data.work.ImageSearch

class CountryImageSearchUseCase(
    private val imageSearch: ImageSearch,
    private val countryDao: CountryDao
) : RxSingleUseCase<Country, Boolean>() {
    override fun doSomething(param: Country): Boolean {
        return if (param.images.isEmpty() && param.name.isNotEmpty()) {
            val images = imageSearch.search(
                param.name
                    .replace(" ", "+")
                    .plus("+flag")
            )
            param.images = images
            countryDao.update(param)
            true
        } else {
            false
        }
    }
}