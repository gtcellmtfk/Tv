package com.bytebyte6.data

import com.bytebyte6.base.logd
import com.bytebyte6.data.dao.CountryDao
import com.bytebyte6.data.work.SearchImage
import com.bytebyte6.data.work.SearchImageImpl

class CountryImageSearch(private val dao: CountryDao,private val searchImage: SearchImage)  {
    fun doThatShit() {
        dao.getCountries()
            //只取为空的查询图片
            .filter {
                it.image.isEmpty()
            }
            //查询到图片后直接插入数据库
            .forEach { country ->
                if (country.name.isNotEmpty()) {
                    country.image = searchImage.search(country.name.plus("+flag"))
                    dao.insert(country)
                }
            }
    }
}