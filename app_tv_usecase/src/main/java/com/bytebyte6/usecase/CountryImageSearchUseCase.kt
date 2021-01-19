package com.bytebyte6.usecase

import com.bytebyte6.base.RxUseCase
import com.bytebyte6.data.dao.CountryDao
import com.bytebyte6.data.entity.Country
import com.bytebyte6.data.work.SearchImage

class CountryImageSearchUseCase(
    private val imageSearch: SearchImage,
    private val countryDao: CountryDao
) : RxUseCase<Country, Boolean>() {
    override fun run(param: Country): Boolean {
        return if (param.image.isEmpty() && param.name.isNotEmpty()) {
            val image = imageSearch.search(param.name.plus("+flag"))
            param.image = image
            countryDao.update(param)
            true
        } else {
            false
        }
    }
}