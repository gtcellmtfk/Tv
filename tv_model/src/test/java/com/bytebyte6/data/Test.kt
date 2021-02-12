package com.bytebyte6.data

import com.bytebyte6.common.GsonConfig
import com.bytebyte6.data.entity.Tv
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import org.junit.Test
import java.io.File

class Test2 {
    private val paths = listOf(
        "C:\\Users\\zacks\\Videos\\zho.m3u",
        "C:\\Users\\zacks\\Videos\\china.m3u8",
        "C:\\Users\\zacks\\Videos\\test.m3u",
        "C:\\Users\\zacks\\Videos\\test2.m3u",
        "C:\\Users\\zacks\\Videos\\央视+卫视+NewTV广西移动源.m3u",
        "C:\\Users\\zacks\\Videos\\mov.m3u",
        "C:\\Users\\zacks\\Videos\\kor.m3u",
        "C:\\Users\\zacks\\Videos\\index.m3u",
        "C:\\Users\\zacks\\Videos\\aqy.m3u",
        "C:\\Users\\zacks\\Videos\\111.m3u"
    )

    private val gson =
        GsonBuilder().registerTypeAdapterFactory(GsonConfig.NullStringToEmptyAdapterFactory())
            .create()

    @Test
    fun test3() {
        val path = "C:\\Users\\zacks\\Downloads\\channels.json"
        val file = File(path)
        val tvs = getTvs(file).map {
            Tv.init(it)
        }

        val countries = tvs.map {
            it.country
        }.toSet()
        val json = gson.toJson(countries)
        val countriesFile = File("C:\\Users\\zacks\\Downloads\\countries.json")
        countriesFile.writeText(json)

        val languages = tvs.map {
            it.language
        }.toSet()
        val json2 = gson.toJson(languages)
        val languagesFile = File("C:\\Users\\zacks\\Downloads\\languages.json")
        languagesFile.writeText(json2)

        val categories = tvs.map {
            it.category
        }.toSet()
        val json3 = gson.toJson(categories)
        val categoriesFile = File("C:\\Users\\zacks\\Downloads\\categories.json")
        categoriesFile.writeText(json3)
    }

    private fun getTvs(file: File): List<Tv> {
        val json: String = file.readText()
        return gson.fromJson(json, object : TypeToken<List<Tv>>() {}.type)
    }

    @Test
    fun testZho() {
        paths.forEach { path ->
            val file = File(path)
            println("-------------------$path---------------------------")
            M3u.getTvs(file).forEach {
                println(it.url)
            }
        }
    }
}