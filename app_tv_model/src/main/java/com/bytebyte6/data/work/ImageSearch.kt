package com.bytebyte6.data.work

import com.bytebyte6.base.logd
import com.bytebyte6.data.dao.CountryDao
import org.jsoup.Jsoup

interface UrlProvider {
    fun provide(key: String): String
}

class GoogleUrlProvider : UrlProvider {
    override fun provide(key: String): String {
        return "https://www.sogou.com/web?query=${key.replace(" ", "+")}+National+flag"
    }
}

class BingUrlProvider : UrlProvider {
    override fun provide(key: String): String {
        return "https://cn.bing.com/images/search?q=${key.replace(" ", "+")}+National+flag"

    }
}

class BaiDuUrlProvider : UrlProvider {
    override fun provide(key: String): String {
        return "https://www.baidu.com/s?ie=utf-8&f=8&rsv_bp=1&rsv_idx=1&tn=baidu&wd=${key.replace(
            " ",
            "+"
        )}&fenlei=256&oq=%25E7%25BE%258E%25E5%259B%25BD%25E5%259B%25BD%25E6%2597%2597&rsv_pq=983eb46300019152&rsv_t=5430y4DtwiLf32MUG%2BQITNHeNsVlju%2Fw2Nhkv39gQOaRAJ6%2Fgf8BJax%2F90o&rqlang=cn&rsv_enter=0&rsv_dl=tb&rsv_btype=t"
    }
}

class CountryImageSearch(private val dao: CountryDao) : ImageSearch() {
    fun doThatShit() {
        dao.getCountries()
                //只取为空的查询图片
            .filter {
                it.images.isEmpty()
            }
                //查询到图片后直接插入数据库
            .map { country ->
                if (country.name.isNotEmpty()) {
                    country.images = search(country.name)
                    logd(country.images.toString())
                    dao.insert(country)
                }
                country
            }
    }
}

open class ImageSearch : SearchAnything {
    override val urls: List<UrlProvider>
        get() = mutableListOf(
            BingUrlProvider(),
            BaiDuUrlProvider(),
            GoogleUrlProvider()
        )

    override fun search(key: String): List<String> = list(key)

    private fun list(key: String): List<String> {
        val images = mutableListOf<String>()
        urls.forEach {
            val realUrl = it.provide(key)
            val doc = Jsoup.connect(realUrl).get()
            val media = doc.select("[src]")
            for (src in media) {
                if (src.normalName() == "img") {
                    val image = src.attr("abs:src")
                    val width = src.attr("width")
                    val height = src.attr("height")
                    if (image.isNotEmpty() && width.isNotEmpty() && height.isNotEmpty()) {
                        images.add(image)
                    }
                }
            }
        }
        return images
    }
}

interface SearchAnything {
    val urls: List<UrlProvider>

    fun search(key: String): List<String>
}

