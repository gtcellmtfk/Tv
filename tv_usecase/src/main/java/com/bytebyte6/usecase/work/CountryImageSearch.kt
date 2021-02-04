package com.bytebyte6.usecase.work

import com.bytebyte6.data.DataManager
import com.bytebyte6.image.SearchImage

class CountryImageSearch(private val dataManager: DataManager, private val searchImage: SearchImage) {
    fun doThatShit() {
        dataManager.getCountries()
            //只取为空的查询图片
            .filter {
                it.image.isEmpty()
            }
            //查询到图片后直接插入数据库
            .forEach { country ->
                if (country.name.isNotEmpty()) {
                    country.image = searchImage.search(country.name.plus("+flag"))
                    dataManager.insertCountry(country)
                }
            }
    }
}