package com.bytebyte6.data

import com.bytebyte6.data.entity.Tv
import com.bytebyte6.data.work.SearchImageImpl
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okio.internal.commonToUtf8String
import org.jsoup.Jsoup
import org.jsoup.select.Elements
import org.junit.Test
import java.io.File

class GetCountryTest {

    @Test
    fun test() {
        val document = Jsoup.connect("https://cn.bing.com/images/search?q=Azerbaijan flag").get()
        val images: Elements = document.select("img[src~=(?i)\\.(png|jpe?g|gif)]")
        for (image in images) {
            println("src : " + image.attr("src"))
        }
    }

    @Test
    fun country2() {
        val searchImageImpl = SearchImageImpl()
        val search = searchImageImpl.search("东南卫视高清")
    }

    private fun list2() {
        val search = SearchImageImpl()
        val sorted = getTvs().map {
            it.country.name
        }.filter {
            it.isNotEmpty()
        }.distinct()
            .sorted()
        val map = mutableMapOf<String, String>()
        sorted
            .forEach {
                map[it] = search.search(it.plus(" flag"))
            }
        map.forEach {
            if (it.value.isEmpty()) {
                println("*********************************************")
                println(it)
            } else {
                println(it)
            }
        }
    }

    private fun getTvs(): List<Tv> {
        val channels = "C:\\Users\\zacks\\Downloads\\channels.json"
        val file = File(channels)
        val gson = Gson()
        val json = file.readBytes().commonToUtf8String()
        return gson.fromJson(json, object : TypeToken<List<Tv>>() {}.type)
    }
}