package com.bytebyte6.data.work

import com.bytebyte6.base.logd
import com.bytebyte6.data.dao.CountryDao
import com.bytebyte6.data.dao.TvDao
import org.jsoup.Jsoup

interface UrlProvider {
    fun provide(key: String): String
}

class SogouUrlProvider : UrlProvider {
    override fun provide(key: String): String {
        return "https://pic.sogou.com/pics?query=${key}"
    }
}

class BingUrlProvider : UrlProvider {
    override fun provide(key: String): String {
        return "https://cn.bing.com/images/search?q=${key}"

    }
}

class BaiDuUrlProvider : UrlProvider {
    override fun provide(key: String): String {
        return "https://image.baidu.com/search/index?tn=baiduimage&word=${key}"
    }
}

class TvLogoSearch(private val dao: TvDao) : ImageSearch() {
    fun doThatShit() {
        dao.getTvs()
            .filter {
                it.logo.isEmpty()
            }
            .map {
                if (it.name.isNotEmpty()) {
                    val list = search(it.name
                        .replace(" ", "+")
                        .plus("+TV+Logo"))
                    if (list.isNotEmpty()) {
                        it.logo = list[0]
                        logd(it.logo)
                        dao.insert(it)
                    }
                }
            }
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
                    country.images = search(
                        country.name
                            .replace(" ", "+")
                            .plus("+flag+国旗")
                    )
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
            SogouUrlProvider()
        )

    override fun search(key: String): List<String> = list(key)

    private fun list(key: String): List<String> {
        val images = mutableListOf<String>()
        urls.forEach {
            val realUrl = it.provide(key)
            logd("readUrl=$realUrl")
            val doc = Jsoup.connect(realUrl).get()
            val media = doc.select("[src]")
            for (src in media) {
                if (src.normalName() == "img") {
                    val image = src.attr("abs:src")
                    val width = src.attr("width")
                    val height = src.attr("height")
                    if (image.isNotEmpty() && width.isNotEmpty() && height.isNotEmpty()) {
                        if (!image.contains("logo"))
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

