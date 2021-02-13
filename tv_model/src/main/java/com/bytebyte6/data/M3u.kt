package com.bytebyte6.data

import com.bytebyte6.data.entity.Category
import com.bytebyte6.data.entity.Country
import com.bytebyte6.data.entity.Language
import com.bytebyte6.data.entity.Tv
import okio.internal.commonToUtf8String
import java.io.File
import java.io.InputStream
import java.util.*

object M3u {
    private const val logoPattern = "(?<=tvg-logo=\").*?(?=\")"
    private const val namePattern = "(?<=tvg-name=\").*?(?=\")"
    private const val namePattern3 = "(?<=\",).*?(?=\n)"
    private const val languagePattern = "(?<=tvg-language=\").*?(?=\")"
    private const val countryPattern = "(?<=tvg-country=\").*?(?=\")"
    private const val categoryPattern = "(?<=group-title=\").*?(?=\")"
    private val logoRegex = Regex(logoPattern)
    private val nameRegex = Regex(namePattern)
    private val nameRegex3 = Regex(namePattern3)
    private val langRegex = Regex(languagePattern)
    private val countryRegex = Regex(countryPattern)
    private val categoryRegex = Regex(categoryPattern)

    fun getTvs(inputStream: InputStream): List<Tv> {
        val m3uString = inputStream.readBytes()
            .commonToUtf8String()
            .removePrefix("#EXTM3U")
            .trim()
        return getTvs(m3uString)
    }

    fun getTvs(m3uFile: File): List<Tv> {
        val m3uString = m3uFile.readBytes()
            .commonToUtf8String()
            .removePrefix("#EXTM3U")
            .trim()
        return getTvs(m3uString)
    }

    private fun getTvs(m3uString: String): List<Tv> {
        val contains = m3uString.contains("tvg-id")
        val nameAndUrlList = if (contains) {
            m3uString.split("#EXTINF:-1 ").toMutableList()
        } else {
            m3uString.split("#EXTINF:-1 ,").toMutableList()
        }
        nameAndUrlList.remove("")
        return if (contains) {
            getTvsByTvg(nameAndUrlList)
        } else {
            getTvsNormal(nameAndUrlList)
        }
    }

    private fun getTvsNormal(nameAndUrlList: List<String>): List<Tv> {
        val tvs = mutableListOf<Tv>()
        for (str in nameAndUrlList) {
            val nameAndUrl = str.split("\n")
            tvs.add(Tv(name = nameAndUrl[0], url = nameAndUrl[1].trim()))
        }
        return tvs
    }

    private fun getTvsByTvg(nameAndUrlList: List<String>): List<Tv> {
        val tvs = mutableListOf<Tv>()
        for (str in nameAndUrlList) {
            val url = if (str.contains("#EXTVLCOPT")) {
                str.split("\n")[2].trim()
            } else {
                str.split("\n")[1].trim()
            }
            val logo = logoRegex.find(str)?.value ?: ""
            var name = nameRegex3.find(str)?.value ?: ""
            if (name.isEmpty()) {
                name = nameRegex.find(str)?.value ?: ""
            }
            if (name.contains("&")) {
                name = name.replace("&", "")
            }
            val lang = langRegex.find(str)?.value ?: ""
            val code = countryRegex.find(str)?.value ?: Country.UNKOWN
            val category = categoryRegex.find(str)?.value ?: Category.OTHER
            tvs.add(
                Tv().apply {
                    this.url = url
                    this.logo = logo
                    this.name = name
                    this.language = lang
                    this.country = Country(code = code.toLowerCase(Locale.ROOT))
                    this.category = category
                }
            )
        }
        return tvs
    }
}