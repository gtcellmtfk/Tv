package com.bytebyte6.usecase

import com.bytebyte6.common.RxUseCase
import com.bytebyte6.data.DataManager
import com.bytebyte6.data.entity.Country
import com.bytebyte6.image.SearchImage

class CountryImageSearchUseCase(
    private val imageSearch: SearchImage,
    private val dataManager: DataManager
) : RxUseCase<Country, Boolean>() {
    override fun run(param: Country): Boolean {
        return if (param.image.isEmpty() && param.name.isNotEmpty()) {
            val image = imageSearch.search(param.name.plus("+flag"))
            param.image = image
            dataManager.updateCountry(param)
            true
        } else {
            false
        }
    }
}