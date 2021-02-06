package com.bytebyte6.usecase.work

import com.bytebyte6.data.DataManager
import com.bytebyte6.image.SearchImage

class SearchCountryImage(
    private val dataManager: DataManager,
    private val searchImage: SearchImage
) {
    fun searchCountryImage() {
        dataManager.getCountries()
            //只取为空的查询图片
            .filter {
                it.image.isEmpty() && it.name.isNotEmpty()
            }
            //查询到图片后直接插入数据库
            .forEach { country ->
                val image = searchImage.search(country.name.plus("+flag"))
                if (image.isNotEmpty()) {
                    country.image = image
                    dataManager.updateCountry(country)
                }
            }
    }
}