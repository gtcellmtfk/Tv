package com.bytebyte6.data

import com.bytebyte6.data.entity.Country
import com.bytebyte6.data.entity.Tv
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okio.internal.commonToUtf8String
import org.jsoup.Jsoup
import org.junit.Test
import java.io.File
import java.io.IOException

class GetCountryTest {

    @Test
    @Throws(IOException::class)
    fun testCountryList() {
        val filter = getTvs().filter {
            it.countryName == "China"
        }
        println(filter.size)
//        getCountryList(getTvs())
    }

    private fun getTvs(): List<Tv> {
        val channels = "C:\\Users\\zacks\\Downloads\\channels.json"
        val file = File(channels)
        val gson = Gson()
        val json = file.readBytes().commonToUtf8String()
        return gson.fromJson(json, object : TypeToken<List<Tv>>() {}.type)
    }

    private fun getCountryList(list: List<Tv>): List<Country> {
        val countryList = mutableListOf<Country>()
        val names = mutableSetOf<String>()
        list.forEach {
            names.add(it.country.name)
        }
        names.forEach {
            val country = getCountry(it)
            countryList.add(country)
            println(country)
        }
        return countryList
    }

    private fun getCountry(key: String): Country {

        val images = mutableListOf<String>()

        val country = Country(name = key,images = images)

        val url = "https://cn.bing.com/images/search?q=${key}+flag"

        val doc = Jsoup.connect(url).get()

        val media = doc.select("[src]")

        for (src in media) {
            if (src.normalName() == "img") {
                val image = src.attr("abs:src")
                if (image.isNotEmpty()) {
                    val width = src.attr("width")
                    val height = src.attr("height")
                    if (width.isNotEmpty() && height.isNotEmpty()){
                        println("width=$width height=$height")
                        images.add(image)
                    }
                }
            }
        }

        return country
    }
}