package com.bytebyte6.usecase.work

import com.bytebyte6.data.DataManager
import com.bytebyte6.data.entity.Country
import com.bytebyte6.image.SearchImage

class CountryImageSearch(
    private val dataManager: DataManager,
    private val searchImage: SearchImage
) {
    fun doThatShit() {
        val list = mutableListOf<Country>()
        dataManager.getCountries()
            //只取为空的查询图片
            .filter {
                it.image.isEmpty() && it.name.isNotEmpty()
            }
            //查询到图片后直接插入数据库
            .forEach { country ->
                country.image = searchImage.search(country.name.plus("+flag"))
                // 频繁更新数据库建议每10个更新一遍
                // dataManager.updateCountry(country)
                list.add(country)
                if (list.size == 10) {
                    dataManager.updateCountry(list)
                    list.clear()
                }
            }
        if (list.isNotEmpty()) {
            dataManager.updateCountry(list)
        }
    }
}